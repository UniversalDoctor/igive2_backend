package com.universaldoctor.igive2.web.rest.dashboard;

import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.User;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.dto.StudyDTO;
import com.universaldoctor.igive2.service.dto.StudyManage;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardStudyResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "study";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final StudyService studyService;
    private final ResearcherRepository researcherRepository;
    private final ParticipantInvitationService participantInvitationService;

    public DashboardStudyResource(UserService userService, StudyService studyService, ResearcherRepository researcherRepository,
                                  ParticipantInvitationService participantInvitationService) {
        this.userService = userService;
        this.studyService = studyService;
        this.researcherRepository = researcherRepository;
        this.participantInvitationService = participantInvitationService;
    }

    /**
     * {@code POST  /study} : create and save a new study and associate to researcher
     *
     * @param study the object to create and save.
     * @return the object
     */
    @PostMapping("/study")
    public ResponseEntity<Study> addStudy(@RequestBody Study study) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            String code =(String) UUID.randomUUID().toString().subSequence(0, 4);
            study.setCode(code);
            if (researcher.isPresent() && studyService.alreadyCodeUsed(study.getCode())==false) {
                return ResponseUtil.wrapOrNotFound(studyService.addAndSaveStudy(study,researcher.get()));
            }
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code PUT  /study} :update the study
     *
     * @param study the object that i want update, updated
     * @return the object
     */
    @PutMapping("/study")
    public ResponseEntity<Study> updateStudy(@RequestBody Study study) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to update Study with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            Optional<Study> optionalStudy=studyService.findOne(study.getId());
            if (researcher.isPresent() && optionalStudy.isPresent()) {
                return ResponseUtil.wrapOrNotFound(studyService.updateStudy(study,optionalStudy.get()));
            }
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code PUT /study/recruiting/{booleano}} : update if the study are recruiting or not
     *
     * @param study the object that i wants update, the need information of the study is only the id
     * @param booleano the conditional, to change if the study are recruiting or not
     * @return true if all it's correctly
     */
    @PutMapping("/study/recruiting/{booleano}")
    public ResponseEntity<Boolean> putRecruiting(@RequestBody Study study, @PathVariable("booleano") boolean booleano) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                Optional<Study> optionalStudy = studyService.findOne(study.getId());
                if (optionalStudy.isPresent() && optionalStudy.get().getResearcher().getId().equals(researcher.get().getId())) {
                    optionalStudy.get().setRecruiting(booleano);
                    studyService.save(optionalStudy.get());
                    participantInvitationService.deleteInvitationsNotAccepted(optionalStudy.get());
                    return new ResponseEntity<>(true,HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET /study} : get all the studies that belong at researcher
     *
     * @return list of studies
     */
    @GetMapping("/study")
    public ResponseEntity<Set<Study>> getStudies() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                return ResponseUtil.wrapOrNotFound(studyService.getStudyByResearcher(researcher.get()));
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET /study/{id}} : get one study that belong at researcher
     *
     * @param  id the id of study that I want get
     * @return the object
     */
    @GetMapping("/study/{id}")
    public ResponseEntity<Study> getStudy(@PathVariable("id") String id) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                return ResponseUtil.wrapOrNotFound(studyService.findOne(id));
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET /studies} : get all the studies that belong at researcher but without institutions and with the activatePartcipants
     *
     * @return list of studies without institutions and with the activatePartcipants
     */
    @GetMapping("/studies")
    public ResponseEntity<ArrayList<StudyDTO>> getStudiesInfo() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                Optional<Set<Study>> studies = studyService.getStudyByResearcher(researcher.get());
                if(studies.isPresent()){
                    Optional<ArrayList<StudyDTO>> studyDTOS= Optional.ofNullable(studyService.getStudiesDTO(studies.get()));
                    return ResponseUtil.wrapOrNotFound(studyDTOS);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET /studyManage/{studyId}} :get an information of study and information of all her participants
     *
     * @param studyId  to get forms and the participants of this study.
     * @return entity with information of study and list of participants with information of them
     */
    @GetMapping("/study/data/{studyId}")
    public ResponseEntity<StudyManage> getStudyManage(@PathVariable("studyId") String studyId) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent() ) {
                Optional<Study> study = studyService.findOne(studyId);
                if (study.isPresent() ){
                    if(study.get().getResearcher().getId().equals(researcher.get().getId())) {
                        Optional<StudyManage> studyManage = Optional.ofNullable(studyService.createStudyManage(study.get()));
                        return ResponseUtil.wrapOrNotFound(studyManage);
                    }
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code DELETE  "/study"} : delete the "study"
     *
     * @param study the object that I want delete
     * @return true if all it's correctly
     */
    @DeleteMapping("/study")
    public ResponseEntity<Boolean> removeStudy(@RequestBody Study study) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to delete Study with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            Optional<Study> optionalStudy=studyService.findOne(study.getId());
            if(researcher.isPresent() && optionalStudy.isPresent()){
                log.debug("\n\nstudy: "+optionalStudy.get()+"\n");
                return new ResponseEntity<>(studyService.deleteStudy(optionalStudy.get(),researcher.get()),HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
