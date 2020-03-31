package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.ParticipantInvitation;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.service.ParticipantInvitationService;
import com.universaldoctor.igive2.service.StudyService;
import com.universaldoctor.igive2.service.UserService;
import com.universaldoctor.igive2.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.universaldoctor.igive2.domain.ParticipantInvitation}.
 */
@RestController
@RequestMapping("/api")
public class ParticipantInvitationResource {

    private final Logger log = LoggerFactory.getLogger(ParticipantInvitationResource.class);

    private static final String ENTITY_NAME = "participantInvitation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipantInvitationService participantInvitationService;

    public ParticipantInvitationResource(ParticipantInvitationService participantInvitationService) {
        this.participantInvitationService = participantInvitationService;
    }

    /**
     * {@code POST  /participant-invitations} : Create a new participantInvitation.
     *
     * @param participantInvitation the participantInvitation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new participantInvitation, or with status {@code 400 (Bad Request)} if the participantInvitation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/participant-invitations")
    public ResponseEntity<ParticipantInvitation> createParticipantInvitation(@Valid @RequestBody ParticipantInvitation participantInvitation) throws URISyntaxException {
        log.debug("REST request to save ParticipantInvitation : {}", participantInvitation);
        if (participantInvitation.getId() != null) {
            throw new BadRequestAlertException("A new participantInvitation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParticipantInvitation result = participantInvitationService.save(participantInvitation);
        return ResponseEntity.created(new URI("/api/participant-invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /participant-invitations} : Updates an existing participantInvitation.
     *
     * @param participantInvitation the participantInvitation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participantInvitation,
     * or with status {@code 400 (Bad Request)} if the participantInvitation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the participantInvitation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/participant-invitations")
    public ResponseEntity<ParticipantInvitation> updateParticipantInvitation(@Valid @RequestBody ParticipantInvitation participantInvitation) throws URISyntaxException {
        log.debug("REST request to update ParticipantInvitation : {}", participantInvitation);
        if (participantInvitation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParticipantInvitation result = participantInvitationService.save(participantInvitation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, participantInvitation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /participant-invitations} : get all the participantInvitations.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of participantInvitations in body.
     */
    @GetMapping("/participant-invitations")
    public ResponseEntity<List<ParticipantInvitation>> getAllParticipantInvitations(Pageable pageable) {
        log.debug("REST request to get a page of ParticipantInvitations");
        Page<ParticipantInvitation> page = participantInvitationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /participant-invitations/:id} : get the "id" participantInvitation.
     *
     * @param id the id of the participantInvitation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the participantInvitation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/participant-invitations/{id}")
    public ResponseEntity<ParticipantInvitation> getParticipantInvitation(@PathVariable String id) {
        log.debug("REST request to get ParticipantInvitation : {}", id);
        Optional<ParticipantInvitation> participantInvitation = participantInvitationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(participantInvitation);
    }

    /**
     * {@code DELETE  /participant-invitations/:id} : delete the "id" participantInvitation.
     *
     * @param id the id of the participantInvitation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/participant-invitations/{id}")
    public ResponseEntity<Void> deleteParticipantInvitation(@PathVariable String id) {
        log.debug("REST request to delete ParticipantInvitation : {}", id);
        participantInvitationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
