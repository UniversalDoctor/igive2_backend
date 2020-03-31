package com.universaldoctor.igive2.web.rest.dashboard;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.dto.Invitations;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardInvitationResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "invitations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResearcherService researcherService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final StudyService studyService;
    private final ParticipantService participantService;
    private final ResearcherRepository researcherRepository;
    private final ParticipantInvitationService participantInvitationService;

    public DashboardInvitationResource(ResearcherService researcherService, UserService userService, UserRepository userRepository, StudyService studyService,
                                       ParticipantService participantService, ResearcherRepository researcherRepository,
                                       ParticipantInvitationService participantInvitationService) {
        this.researcherService = researcherService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.studyService = studyService;
        this.participantService = participantService;
        this.researcherRepository = researcherRepository;
        this.participantInvitationService = participantInvitationService;
    }

    /**
     * {@code POST  /invitations/{studyId}} : create and save new invitation (possible participation)
     *
     * @param studyId the id of the study to which I want to invite it
     * @param participantInvitation the object
     * @return the object.
     */

    @PostMapping("/invitations/{studyId}")
    public ResponseEntity<ParticipantInvitation> addInvitations(@RequestBody ParticipantInvitation participantInvitation,
                                                                @PathVariable("studyId") String studyId) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to add invitation with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            Optional<Study> study=studyService.findOne(studyId);
            Optional<ParticipantInvitation> invitation=participantInvitationService.getFromEmailAndStudy(participantInvitation.getEmail(),study.get());
            if(invitation.isPresent() && invitation.get()!=null){
                ResponseEntity error=new ResponseEntity("already have a invitation with this email", HttpStatus.ALREADY_REPORTED);
                return error;
            }
            if(participantInvitation.getEmail()==user.get().getEmail()){
                ResponseEntity error=new ResponseEntity("you can not invite yourself", HttpStatus.CONFLICT);
                return error;
            }
            if (researcher.isPresent() && study.isPresent() && study.get().getState()== State.PUBLISHED) {
                Optional<ParticipantInvitation> result=participantInvitationService.addAndSaveInvitation(studyId,participantInvitation,researcher.get(),user.get());
                return ResponseUtil.wrapOrNotFound(result);
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code POST  /invitations} : create and save many new invitations (possible participation), you can not create another
     * invitation if  it is created before, and you can not invitate yourself but the method do not send an error only
     * do not do it
     *
     * @param invitations have the study id to can get study for then can send invitation to the list of mails that it have too
     * @return list of invitations.
     */

    @PostMapping("/invitations")
    public ResponseEntity<Set<ParticipantInvitation>> addInvitationsFromArray(@RequestBody Invitations invitations) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to add invitation with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if(researcher.isPresent()) {
                invitations.setResearcher(researcher.get());
                invitations.setUser(user.get());
                return participantInvitationService.addAndSaveManyInvitation(invitations);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code GET /study/{idStudy}/invitations} : get all  the Invitations of one study
     *
     * @param idStudy the id of study where the invitations be
     * @return list of invitations
     */
    @GetMapping("/study/{idStudy}/invitations")
    public ResponseEntity<Set<ParticipantInvitation>> getInvitations(@PathVariable("idStudy") String idStudy) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get invitation with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                Optional<Study> optionalStudy = studyService.findOne(idStudy);
                if (optionalStudy.isPresent() && optionalStudy.get().getResearcher().getId().equals(researcher.get().getId())) {
                    return ResponseUtil.wrapOrNotFound(participantInvitationService.getFromStudy(optionalStudy.get()));
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET study/{idStudy}/participant} : get all the participants of study
     *
     * @param idStudy the id of study, that I want consult, to get the participants
     * @return list of participants
     */
    @GetMapping("/study/{idStudy}/participant")
    public ResponseEntity<Set<Participant>> getParticipants(@PathVariable("idStudy") String idStudy) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                Optional<Study> optionalStudy = studyService.findOne(idStudy);
                if (optionalStudy.isPresent() && optionalStudy.get().getResearcher().getId().equals(researcher.get().getId())) {
                    return ResponseUtil.wrapOrNotFound(participantService.getFromStudy(optionalStudy.get()));
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }


    /**
     * {@code DELETE  "//invitations/{invitationId}"} : delete the "invitation"
     *
     * @param invitationId the id of invitation that I want delete
     * @return true if it's correctly
     */

    @DeleteMapping("/invitations/{invitationId}")
    public ResponseEntity<Boolean> removeInvitations(@PathVariable("invitationId") String invitationId) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if(researcher.isPresent()){
                log.debug("REST request to delete invitation with researcher : {}", user.get().getLogin());
                Optional<ParticipantInvitation> invitation=participantInvitationService.findOne(invitationId);
                if (invitation.isPresent() && invitation.get()!=null ){
                    if(invitation.get().getParticipantId()!=null) {
                        Optional<Participant> participant = participantService.findOne(invitation.get().getParticipantId());
                        if (participant.isPresent() && participant.get() != null) {
                            Optional<User> mobile = userRepository.findById(participant.get().getMobileUser().getUserId());
                            Optional<Study> study = studyService.findOne(participant.get().getStudy().getId());
                            if (mobile.isPresent() && study.isPresent()) {
                                userService.sendMailAbandonStudy(mobile.get(), study.get(), invitation.get().getEmail());
                                participantInvitationService.deleteInvitation(invitation.get(), researcher.get());
                                return new ResponseEntity<>(true, HttpStatus.OK);
                            }
                        }
                    }
                    participantInvitationService.delete(invitationId);
                    ResponseEntity error=new ResponseEntity("delete only the participant invitation but not the participant " +
                        "because do not have any participant for this invitation", HttpStatus.OK);
                    return error;
                }
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
