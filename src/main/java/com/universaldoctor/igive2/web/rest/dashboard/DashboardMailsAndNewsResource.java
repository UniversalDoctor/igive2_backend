package com.universaldoctor.igive2.web.rest.dashboard;

import com.universaldoctor.igive2.domain.ParticipantInvitation;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.User;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.dto.MailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardMailsAndNewsResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "researcher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResearcherService researcherService;
    private final UserService userService;
    private final ParticipantInvitationService participantInvitationService;
    private final StudyService studyService;

    public DashboardMailsAndNewsResource(ResearcherService researcherService, UserService userService, ParticipantInvitationService participantInvitationService, StudyService studyService) {
        this.researcherService = researcherService;
        this.userService = userService;
        this.participantInvitationService = participantInvitationService;
        this.studyService = studyService;
    }

    /**
     * {@code POST /mail} : send to all participants of study
     *
     * @param mailDTO to get the study to can get all the emails of participant, the subject and the body of the mail
     * @return true if all it's correctly
     */
    @PostMapping("/mail")
    public ResponseEntity<Boolean> sendEmail(@RequestBody MailDTO mailDTO) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to send email with researcher : {}", user.get().getLogin());
            mailDTO.setResearcherMail(user.get().getEmail());
            researcherService.sendMailAllParticipants(mailDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code POST /mail/resend/{idInvitation} : send or reseand mail of invitation to participant only send email when the invitation
     * is in state false
     *
     * @param idInvitation  the id of the participant invitation that i want send o reseand the mail
     * @return if send the email return true if not return false
     */
    @PostMapping("/mail/resend/{idInvitation}")
    public ResponseEntity<Boolean> resendEmail(@PathVariable ("idInvitation") String idInvitation) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to send email with researcher : {}", user.get().getLogin());
            Optional<ParticipantInvitation> invitation = participantInvitationService.findOne(idInvitation);
            if(invitation.isPresent() && invitation.get()!=null && !invitation.get().isState()){
                Optional<Study> study= studyService.findOne(invitation.get().getStudy().getId());
                if(study.isPresent() && study.get()!=null && study.get().getState()== State.PUBLISHED && study.get().isRecruiting()) {
                    userService.sendMailInvitation(user.get(), study.get(), invitation.get().getEmail());
                    return new ResponseEntity<>(true,HttpStatus.OK);
                }
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(false,HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(false,HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
}
