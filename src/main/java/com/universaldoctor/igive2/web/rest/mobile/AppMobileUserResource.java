package com.universaldoctor.igive2.web.rest.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.security.jwt.JWTFilter;
import com.universaldoctor.igive2.security.jwt.TokenProvider;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.dto.*;
import com.universaldoctor.igive2.web.rest.errors.InvalidPasswordException;
import com.universaldoctor.igive2.service.dto.ManagedUserVM;
import com.universaldoctor.igive2.web.rest.vm.LoginVM;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RestController
@RequestMapping("/api/mobile")
public class AppMobileUserResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "mobileUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MobileUserService mobileUserService;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final IGive2UserService iGive2UserService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MobileUserRepository mobileUserRepository;
    private final MailService mailService;

    public AppMobileUserResource(MobileUserService mobileUserService, TokenProvider tokenProvider, UserService userService,
                                 IGive2UserService iGive2UserService, AuthenticationManagerBuilder authenticationManagerBuilder,
                                 MobileUserRepository mobileUserRepository, MailService mailService) {

        this.mobileUserService = mobileUserService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.iGive2UserService = iGive2UserService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.mobileUserRepository = mobileUserRepository;
        this.mailService = mailService;
    }

    //cosas necesarias
    //******************************************************************************************************************
    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    //******************************************************************************************************************

    /**
     * {@code POST  /register/{newsLetter} : register the mobileUser, create the user, the igive2user and the mobile user .
     *
     * @param managedUserVM the managed user View Model.
     * @param newsLetter    boolean with acceptance of newsletter
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register/{newsLetter}")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM, @PathVariable("newsLetter") boolean newsLetter) throws URISyntaxException {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        if (managedUserVM.getLangKey() == null || managedUserVM.getLangKey().length() == 0) {
            managedUserVM.setLangKey("en");
        }

        com.universaldoctor.igive2.domain.User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());

        IGive2User iGive2User = iGive2UserService.create(newsLetter, "ES");
        mobileUserService.create(iGive2User,user.getId());

        mailService.sendActivationEmail(user);

    }

    /**
     * {@code POST  /authenticate} : get token only if you are register and  a mobileUser.
     *
     * @param loginVM  to get username and password
     * @return  token to get authentication to others calls
     */
    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        loginVM.setUsername(loginVM.getUsername().toLowerCase());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(loginVM.getUsername());
        Optional<MobileUser> mobileUser=mobileUserRepository.findOneByUserId(user.get().getId());
        if(user.isPresent() && mobileUser.isPresent()) {
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    /**
     * {@code PUT  /profile} : update the mobile user with the information of a profile(profile= username, icon, status)
     *
     * @param profile  to the information to update
     * @return true if all it's correctly
     */
    @PutMapping("/profile")
    public ResponseEntity<Boolean> setProfile(@RequestBody Profile profile) {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        Optional<MobileUser> muser=mobileUserRepository.findOneByUserId(user.get().getId());
        if (user.isPresent() && muser.isPresent()) {
            mobileUserService.setProfile(muser.get(), profile);
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code PUT /profile/{condition}} : update the state of newsLetter
     *
     * @param condition the state that i want.
     * @return igive2user
     */
    @PutMapping("/profile/{condition}")
    public ResponseEntity<IGive2User> updateIGive2User(@PathVariable("condition") boolean condition) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user =userService.checkAuthoritation();
        Optional<MobileUser> mobileUser=mobileUserRepository.findOneByUserId(user.get().getId());
        Optional<IGive2User> optionalIGive2User=iGive2UserService.findOne(mobileUser.get().getIGive2User().getId());
        if (user.isPresent() && mobileUser.isPresent() && optionalIGive2User.isPresent()) {
            optionalIGive2User.get().setNewsletter(condition);
            iGive2UserService.save(optionalIGive2User.get());
            return ResponseUtil.wrapOrNotFound(optionalIGive2User);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code PUT /profile/mobileUser} : update the MobileUser from secutiry context
     *
     * @param mobileUser the mobileUser to update.
     * @return mobileUser
     */
    @PutMapping("/profile/mobileUser")
    public ResponseEntity<MobileUser> updateProfile(@RequestBody MobileUser mobileUser) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        Optional<MobileUser> muser=mobileUserRepository.findOneByUserId(user.get().getId());
        if (user.isPresent() && muser.isPresent()) {
             return ResponseUtil.wrapOrNotFound(mobileUserService.update(muser.get(),mobileUser));
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * {@code PUT /setUp} : update the MobileUser from secutiry context
     *
     * @param setUp the information of mobileUser to update.
     * @return true it's all are sucessfull
     */
    @PutMapping("/profile/setUp")
    public ResponseEntity<Boolean> setUp(@RequestBody SetUp setUp) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        Optional<MobileUser> muser=mobileUserRepository.findOneByUserId(user.get().getId());
        if (user.isPresent() && muser.isPresent()) {
            mobileUserService.setUp(muser.get(),setUp);
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.NOT_MODIFIED);
    }

    /**
     * {@code GET /profile} : get profile of mobileuser (profile= username, icon, status)
     *
     * @return profile
     */
    @GetMapping("/profile")
    public ResponseEntity<Profile> getProfile() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        Optional<MobileUser> muser=mobileUserRepository.findOneByUserId(user.get().getId());
        if (user.isPresent() && muser.isPresent()) {
            Optional<Profile> result= Optional.ofNullable(mobileUserService.getProfile(muser.get()));
            return ResponseUtil.wrapOrNotFound(result);
        }
        return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    /**
     * {@code GET /profile/info} : get the MobileUser with a reduced information from secutiry context
     *
     * @return setup
     */
    @GetMapping("/profile/info")
    public ResponseEntity<SetUp> getSetUp() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        Optional<MobileUser> muser=mobileUserRepository.findOneByUserId(user.get().getId());
        if (user.isPresent() && muser.isPresent()) {
            Optional<SetUp> result= Optional.ofNullable(mobileUserService.getSetUp(muser.get()));
            return ResponseUtil.wrapOrNotFound(result);
        }
        return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    /**
     * {@code GET /profile} : get the MobileUser from secutiry context
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @GetMapping("/profile/mobileUser")
    public ResponseEntity<MobileUser> getMobileUser() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> ruser = userService.checkAuthoritation();
        if (ruser.isPresent()) {
            log.debug("REST request to delete MobileUser : {}");
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(ruser.get().getId());
            return ResponseUtil.wrapOrNotFound(mobileUser);
        }
        return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    /**
     * {@code DELETE /profile} : delete the MobileUser from secutiry context
     */
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteMobileUser() throws URISyntaxException {
        log.debug("REST request to delete MobileUser : {}");
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            if (mu.isPresent()) {
                userService.sendMailDeleteAccount(user.get(),user.get().getEmail());
                mobileUserService.deleteProfile(mu.get());
                ResponseEntity ok=new ResponseEntity("profile is deleted",HttpStatus.OK);
                return ok;
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }



}

