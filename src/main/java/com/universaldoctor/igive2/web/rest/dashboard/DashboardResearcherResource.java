package com.universaldoctor.igive2.web.rest.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardResearcherResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "researcher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResearcherService researcherService;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final IGive2UserService iGive2UserService;
    private final MailService mailService;
    private final ResearcherRepository researcherRepository;

    public DashboardResearcherResource(ResearcherService researcherService, UserService userService, TokenProvider tokenProvider,
                                       AuthenticationManagerBuilder authenticationManagerBuilder, IGive2UserService iGive2UserService,
                                       MailService mailService, ResearcherRepository researcherRepository) {
        this.researcherService = researcherService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.iGive2UserService = iGive2UserService;
        this.mailService = mailService;
        this.researcherRepository = researcherRepository;
    }

    // cosas que necesito
    /******************************************************************************************************************/
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

    /******************************************************************************************************************/


    /**
     * {@code POST  /register : register new user.
     *
     * @param researcherDTO the managed user View Model and the information of researcher.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount( @RequestBody ResearcherDTO researcherDTO) throws URISyntaxException {

        ManagedUserVM managedUserVM=researcherService.passOfResearcherDTOtoManagedUser(researcherDTO);

        if (researcherDTO.getLogin() == null || researcherDTO.getLogin().length() == 0){
            managedUserVM.setLogin(managedUserVM.getEmail());
        }
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        if (managedUserVM.getLangKey() == null || managedUserVM.getLangKey().length() == 0) {
            managedUserVM.setLangKey("en");
        }

        com.universaldoctor.igive2.domain.User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());

        IGive2User iGive2User = iGive2UserService.create(false,researcherDTO.getCountry());

        Researcher researcher = researcherService.create(researcherDTO.getInstitution(),researcherDTO.getTitle(),user.getId(),iGive2User);

        mailService.sendActivationEmail(user);
    }



    /**
     * {@code POST  /authenticate} : get token only if you are register and  a researcher
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
        Optional<Researcher> researcher=researcherRepository.findOneByUserId(user.get().getId());
        if(user.isPresent() && researcher.isPresent()) {
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    /**
     * {@code PUT /profile} : update the researcher from secutiry context
     *
     * @param dashboardProfile the object collect information of user, igive2user and researcher
     * @return true if all it's correctly
     */
    @PutMapping("/board/researcher")
    public ResponseEntity<Boolean> updateResearcher(@RequestBody DashboardProfile dashboardProfile) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        Optional<Researcher> researcher=researcherRepository.findOneByUserId(user.get().getId());
        if (user.isPresent() && researcher.isPresent()) {
            log.debug("REST request to delete MobileUser : {}", user.get().getLogin());
            Optional<DashboardProfile> result= Optional.ofNullable(researcherService.setResearcher(dashboardProfile,researcher.get().getId()));
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    /**
     * {@code GET /board/researcher} : get the Researcher from secutiry context
     *
     * @return the researcher
     */
    @GetMapping("/board")
    public ResponseEntity<Researcher> getResearcher() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
            if (user.isPresent()) {
                Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
                return ResponseUtil.wrapOrNotFound(researcher);
            }
        return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    /**
     * {@code GET /researcher} : get the Researcher with the information of user and igive2user
     *
     * @return the researcher with the information of user and igive2user
     */
    @GetMapping("/researcher")
    public ResponseEntity<DashboardProfile> getResearcherProfile() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
        if (user.isPresent() && researcher.isPresent()) {
            Optional<DashboardProfile> result= Optional.ofNullable(researcherService.getResearcher(researcher.get()));
            return ResponseUtil.wrapOrNotFound(result);
        }
        return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    /**
     * {@code DELETE /profile} : delete the researcher, user and igive2user  from secutiry context
     */
    @DeleteMapping("/board")
    public ResponseEntity<Void> deleteResearcher() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
            if (user.isPresent()) {
                log.debug("REST request to delete Researcher : {}", user.get().getLogin());
                Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
                if (researcher.isPresent()) {
                    userService.sendMailDeleteAccount(user.get(),user.get().getEmail());
                    researcherService.deleteResearcher(researcher.get());
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }
}
