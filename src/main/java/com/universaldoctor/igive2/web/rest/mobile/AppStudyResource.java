package com.universaldoctor.igive2.web.rest.mobile;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.repository.FormAnswersRepository;
import com.universaldoctor.igive2.repository.FormRepository;
import com.universaldoctor.igive2.repository.MobileUserRepository;
import com.universaldoctor.igive2.security.jwt.TokenProvider;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.dto.FormAnswerDTO;
import com.universaldoctor.igive2.service.dto.StudyManageMobile;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/mobile")
public class AppStudyResource {

    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "study";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipantService participantService;
    private final StudyService studyService;
    private final UserService userService;
    private final FormService formService;
    private final MobileUserRepository mobileUserRepository;
    private final FormAnswersService formAnswersService;

    public AppStudyResource(ParticipantService participantService, StudyService studyService, UserService userService,
                            FormService formService, MobileUserRepository mobileUserRepository, FormAnswersService formAnswersService) {
        this.participantService = participantService;
        this.studyService = studyService;
        this.userService = userService;
        this.formService = formService;
        this.mobileUserRepository = mobileUserRepository;
        this.formAnswersService = formAnswersService;
    }

    /**
     * {@code GET /studyCompletedForms/{idStudy}} : get the info of study, and all his forms, with his information if it are completed or not, name, etc
     *
     * @param idStudy tto get study and his forms
     * @return object studyManageMobile
     */
    @GetMapping("/studyCompletedForms/{idStudy}")
    public ResponseEntity<StudyManageMobile> getFormsInfo(@PathVariable("idStudy") String idStudy) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(user.get().getId());
            Optional<Study> study=studyService.findOne(idStudy);
            log.debug("\n getformsinfo\n");
            if (mobileUser.isPresent() && study.isPresent()) {
                Optional<Participant> optionalParticipant = participantService.getParticipantFromMobileAndStudy(mobileUser.get(), study.get());
                Optional<Set<Form>> forms =formService.getFormByStudyAndState(study.get(), State.PUBLISHED);
                if(optionalParticipant.isPresent() && forms.isPresent()){
                    Optional<StudyManageMobile> studyManage= Optional.ofNullable(studyService.createStudyManageMobile(study.get(), mobileUser.get()));
                    return ResponseUtil.wrapOrNotFound(studyManage);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET /studyForms/{idForm}} : get the info of form and his questions with his respective answers
     *
     * @param idForm to get form and his questions, and his answers
     * @return object FormAnswerDTO
     */
    @GetMapping("/studyForms/{idForm}")
    public ResponseEntity<FormAnswerDTO> getForm(@PathVariable("idForm") String idForm) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(user.get().getId());
            Optional<Form> form = formService.findOne(idForm);
            if (mobileUser.isPresent() && form.isPresent()) {
                Optional<Participant> optionalParticipant = participantService.getParticipantFromMobileAndStudy(mobileUser.get(), form.get().getStudy());
                if (optionalParticipant.isPresent()) {
                    Optional<FormAnswerDTO> formAnswerDTO = Optional.ofNullable(formAnswersService.getAnswers(form.get(), optionalParticipant.get()));
                    return ResponseUtil.wrapOrNotFound(formAnswerDTO);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
}
