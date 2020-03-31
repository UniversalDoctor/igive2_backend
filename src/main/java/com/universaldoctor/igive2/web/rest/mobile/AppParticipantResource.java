package com.universaldoctor.igive2.web.rest.mobile;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.dto.ParticipantDTO;
import com.universaldoctor.igive2.service.dto.StudyDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/mobile")
public class AppParticipantResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "participant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipantService participantService;
    private final StudyService studyService;
    private final UserService userService;
    private final ParticipantInvitationRepository participantInvitationRepository;
    private final MobileUserRepository mobileUserRepository;

    public AppParticipantResource(ParticipantService participantService, StudyService studyService,
                                  UserService userService, ParticipantInvitationRepository participantInvitationRepository,
                                  MobileUserRepository mobileUserRepository) {

        this.participantService = participantService;
        this.studyService = studyService;
        this.userService = userService;
        this.participantInvitationRepository = participantInvitationRepository;
        this.mobileUserRepository = mobileUserRepository;
    }

    /**
     * {@code POST  /study/{studyCode} : create a new study participant
     *
     * @param studyCode the code of the study to get study and
     * for check if the study are recruiting, if the user have an invitation and if the user is already participant.
     * @return the new participant.
     */
    @PostMapping("/study/{studyCode}")
    public ResponseEntity<Study> addStudy(@PathVariable("studyCode") String studyCode) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to add Study in the MobileUser : {}", user.get().getLogin());
            Optional<Study> study = studyService.getStudyByCode(studyCode);
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            if (mu.isPresent() && study.isPresent() && study.get()!=null) {
                boolean existe = studyService.alreadyParticipate(mu.get(), studyCode);
                if (existe == true ) {
                    ResponseEntity error=new ResponseEntity("already participate in this study",HttpStatus.ALREADY_REPORTED);
                    return error;
                }
                Optional<ParticipantInvitation> invitation=participantInvitationRepository.findOneByEmailAndStudy(user.get().getEmail(),study.get());
                if(!invitation.isPresent() || invitation.get()==null){
                    ResponseEntity error=new ResponseEntity("this mobile user don't have an invitation ", HttpStatus.UNAUTHORIZED);
                    return error;
                }
                if(!study.get().isRecruiting()){
                    ResponseEntity error=new ResponseEntity("the study is not recruiting",HttpStatus.CONFLICT);
                    return error;
                }if(study.get().getState()!= State.PUBLISHED){
                    ResponseEntity error=new ResponseEntity("the study is not published ",HttpStatus.CONFLICT);
                    return error;
                }
            }
            if (study.isPresent() && mu.isPresent() ) {
                Optional<ParticipantInvitation> invitation=participantInvitationRepository.findOneByEmailAndStudy(user.get().getEmail(),study.get());
                participantService.create(mu.get(), study.get(), invitation.get());
                return ResponseUtil.wrapOrNotFound(study);
            }
            ResponseEntity error=new ResponseEntity("this code is not correspond to any study",HttpStatus.CONFLICT);
            return error;
        }
        return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }
    /**
     * {@code GET  /studyInfo/:studyCode} : get a information of one study
     *
     * @param studyCode for get the study.
     * @return information of study.
     */
    @GetMapping("/studyInfo/{studyCode}")
    public ResponseEntity<StudyDTO> getStudyByCode(@PathVariable("studyCode") String studyCode) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to add Study in the MobileUser : {}", user.get().getLogin());
            Optional<Study> study = studyService.getStudyByCode(studyCode);
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            if (mu.isPresent() && study.isPresent() && study.get()!=null) {
                boolean existe = studyService.alreadyParticipate(mu.get(), studyCode);
                if (existe == true ) {
                    ResponseEntity error=new ResponseEntity("already participate in this study",HttpStatus.ALREADY_REPORTED);
                    return error;
                }
                Optional<ParticipantInvitation> invitation=participantInvitationRepository.findOneByEmailAndStudy(user.get().getEmail(),study.get());
                if(!invitation.isPresent() || invitation.get()==null){
                    ResponseEntity error=new ResponseEntity("this mobile user don't have an invitation ", HttpStatus.UNAUTHORIZED);
                    return error;
                }
                if(!study.get().isRecruiting()){
                    ResponseEntity error=new ResponseEntity("the study is not recruiting",HttpStatus.CONFLICT);
                    return error;
                }if(study.get().getState()!= State.PUBLISHED){
                    ResponseEntity error=new ResponseEntity("the study is not published ",HttpStatus.CONFLICT);
                    return error;
                }
            }
            if (study.isPresent() && mu.isPresent() ) {
                Optional<StudyDTO> studyDTO= Optional.ofNullable(studyService.getDTO(study.get()));
                return ResponseUtil.wrapOrNotFound(studyDTO);
            }
            ResponseEntity error=new ResponseEntity("this code is not correspond to any study",HttpStatus.CONFLICT);
            return error;
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
    /**
     * {@code GET /study} : get all the participants of mobile user
     *
     * @return the participants with a reduce information
     */
    @GetMapping("/study")
    public ResponseEntity<ArrayList<ParticipantDTO>> getStudies() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> ruser = userService.checkAuthoritation();
        if (ruser.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(ruser.get().getId());
            if (mobileUser.isPresent()) {
                Optional<Set<Participant>> participants =participantService.getParticipants(mobileUser.get());
                if(participants.isPresent()){
                    Optional<ArrayList<ParticipantDTO>> result= Optional.ofNullable(participantService.getParticipantsDTO(participants.get()));
                    return ResponseUtil.wrapOrNotFound(result);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    /**
     * {@code GET /study/{idStudy}} : get one participants  of mobile user
     *
     * @param idStudy to get the participant that it is participating in this study
     * @return participant with reduce information
     */
    @GetMapping("/study/{idStudy}")
    public ResponseEntity<ParticipantDTO> getStudy(@PathVariable("idStudy") String idStudy) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> ruser = userService.checkAuthoritation();
        if (ruser.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(ruser.get().getId());
            Optional<Study> study=studyService.findOne(idStudy);
            if (mobileUser.isPresent() && study.isPresent() && study.get()!=null) {
                Optional<Participant> participant = participantService.getParticipantFromMobileAndStudy(mobileUser.get(),study.get());
                if(participant.isPresent() && participant.get()!=null) {
                    Optional<ParticipantDTO> participantDTO = Optional.ofNullable(participantService.createDTO(participant.get()));
                    return ResponseUtil.wrapOrNotFound(participantDTO);
                }
                ResponseEntity error=new ResponseEntity("the participant does not present",HttpStatus.NOT_FOUND);
                return error;
            }
            ResponseEntity error=new ResponseEntity("the id of study does not correspond to any study or mobile user is not present",HttpStatus.NOT_FOUND);
            return error;
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code DELETE  /study/{idParticipant}} : delete the participation
     *
     * @param idParticipant the id of participant
     * @return true if removed correctly
     */
    @DeleteMapping("/study/{idParticipant}")
    public ResponseEntity<Boolean> deleteStudy(@PathVariable("idParticipant") String idParticipant) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to delete healthData to MobileUser : {}", user.get().getLogin());
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            Optional<Participant> optionalParticipant=participantService.findOne(idParticipant);
            Optional<Study> study=studyService.findOne(optionalParticipant.get().getStudy().getId());
            if (mu.isPresent() && optionalParticipant.isPresent() && study.isPresent()) {
                userService.sendMailAbandonStudy(user.get(),study.get(),user.get().getEmail());
                return participantService.deleteParticipation(optionalParticipant.get(),mu.get());
            }
            ResponseEntity error=new ResponseEntity("the mobileuser or participant or study are not present, " +
                "try again with another id of participant",HttpStatus.CONFLICT);
            return error;
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
