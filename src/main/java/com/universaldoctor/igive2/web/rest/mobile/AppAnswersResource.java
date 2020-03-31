package com.universaldoctor.igive2.web.rest.mobile;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.repository.MobileUserRepository;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.dto.AnswerMobile;
import com.universaldoctor.igive2.service.dto.ParticipantAnswers;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/mobile")
public class AppAnswersResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "answers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipantService participantService;
    private final UserService userService;
    private final FormService formService;
    private final MobileUserRepository mobileUserRepository;
    private final FormAnswersService formAnswersService;
    private final StudyService studyService;

    public AppAnswersResource(ParticipantService participantService, UserService userService, FormService formService, MobileUserRepository mobileUserRepository, FormAnswersService formAnswersService, StudyService studyService) {
        this.participantService = participantService;
        this.userService = userService;
        this.formService = formService;
        this.mobileUserRepository = mobileUserRepository;
        this.formAnswersService = formAnswersService;
        this.studyService = studyService;
    }

    /**
     * {@code POST /participantAnswers} : add or update answers (create or update a formanswer)
     *
     * @param participantAnswers the information of participant, forms, questions and answers
     * @return form answer
     */
    @PostMapping("/participantAnswers")
    public ResponseEntity<FormAnswers> addAnswers(@RequestBody ParticipantAnswers participantAnswers) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(user.get().getId());
            Optional<Form> form= formService.findOne(participantAnswers.getFormId());
            Optional<Study> study= Optional.ofNullable(form.get().getStudy());
            Optional<Participant> participant=participantService.findOne(participantService.getParticipantFromMobileAndStudy(mobileUser.get(),study.get()).get().getId());
            Optional<ArrayList<AnswerMobile>> answers= Optional.ofNullable(participantAnswers.getResponses());
            if(mobileUser.isPresent() && participant.isPresent() && form.isPresent() && answers.isPresent()){
                return formAnswersService.saveResponses(answers.get(),form.get(),participant.get());
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    /**
     * {@code GET /participantAnswers} : register the user.
     *
     * @param form the form to show all
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @GetMapping("/participantAnswers/{participantId}")
    public ResponseEntity<FormAnswers> getForms(@RequestBody Form form) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(user.get().getId());
            Optional<Form> optionalForm= formService.findOne(form.getId());
            Optional<Study> study= studyService.getStudyByCode(optionalForm.get().getStudy().getCode());
            Optional<Participant> participant=participantService.findOne(participantService.getParticipantFromMobileAndStudy(mobileUser.get(),study.get()).get().getId());
            if(mobileUser.isPresent() && participant.isPresent() && optionalForm.isPresent() ){
                return ResponseUtil.wrapOrNotFound(formAnswersService.getFromParticipantAndForm(optionalForm.get(),participant.get()));
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
