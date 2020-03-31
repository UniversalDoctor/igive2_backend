package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.User;
import com.universaldoctor.igive2.repository.MobileUserRepository;
import com.universaldoctor.igive2.repository.ResearcherRepository;
import com.universaldoctor.igive2.security.jwt.JWTFilter;
import com.universaldoctor.igive2.security.jwt.TokenProvider;
import com.universaldoctor.igive2.service.UserService;
import com.universaldoctor.igive2.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    //puesto a posteriori
    private final ResearcherRepository researcherRepository;
    private final MobileUserRepository mobileUserRepository;
    private final UserService userService;
    //hasta aqui
    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
     /*pueto a posteriori*/  ResearcherRepository researcherRepository,MobileUserRepository mobileUserRepository,UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        //puesto a posteriori
        this.mobileUserRepository=mobileUserRepository;
        this.researcherRepository=researcherRepository;
        this.userService=userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        // puesto a posteriori
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(loginVM.getUsername());
        Optional<Researcher> researcher=researcherRepository.findOneByUserId(user.get().getId());
        Optional<MobileUser> mobileUser=mobileUserRepository.findOneByUserId(user.get().getId());
        if(researcher.isPresent() || mobileUser.isPresent() ){
            return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
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
}
