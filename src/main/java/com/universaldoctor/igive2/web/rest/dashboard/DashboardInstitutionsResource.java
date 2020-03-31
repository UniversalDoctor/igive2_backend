package com.universaldoctor.igive2.web.rest.dashboard;

import com.universaldoctor.igive2.domain.ParticipantInstitution;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.User;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.security.jwt.TokenProvider;
import com.universaldoctor.igive2.service.*;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardInstitutionsResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "institutions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final StudyService studyService;
    private final ResearcherRepository researcherRepository;
    private final ParticipantInstitutionService participantInstitutionService;

    public DashboardInstitutionsResource(UserService userService, StudyService studyService, ResearcherRepository researcherRepository,
                                         ParticipantInstitutionService participantInstitutionService) {
        this.userService = userService;
        this.studyService = studyService;
        this.researcherRepository = researcherRepository;
        this.participantInstitutionService = participantInstitutionService;
    }

    /**
     * {@code GET /institutions} : get all the institutions of one study
     *
     * @param idStudy the id of study that i want consult, to get the institutions
     * @return list of institutions
     */
    @GetMapping("study/{idStudy}/institutions")
    public ResponseEntity<Set<ParticipantInstitution>> getInstitutions(@PathVariable("idStudy") String idStudy) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                Optional<Study> optionalStudy = studyService.findOne(idStudy);
                if (optionalStudy.isPresent() && optionalStudy.get().getResearcher().getId().equals(researcher.get().getId())) {
                    return ResponseUtil.wrapOrNotFound(participantInstitutionService.getInstitutions(optionalStudy.get()));
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code DELETE  "/institutions/{id}"} : delete the "institution"
     *
     * @param id of institution that I want delete.
     * @return true if all it's correctly
     */
    @DeleteMapping("/institutions/{id}")
    public ResponseEntity<Boolean> removeInstitutions(@PathVariable String id) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to delete institution with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                participantInstitutionService.deleteInstitution(id,researcher.get());
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

    }
}
