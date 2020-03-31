package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.domain.ParticipantInstitution;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.service.ParticipantInstitutionService;
import com.universaldoctor.igive2.service.StudyService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.universaldoctor.igive2.domain.ParticipantInstitution}.
 */
@RestController
@RequestMapping("/api")
public class ParticipantInstitutionResource {

    private final Logger log = LoggerFactory.getLogger(ParticipantInstitutionResource.class);

    private static final String ENTITY_NAME = "participantInstitution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipantInstitutionService participantInstitutionService;
    private final StudyService studyService;

    public ParticipantInstitutionResource(ParticipantInstitutionService participantInstitutionService,StudyService studyService) {
        this.participantInstitutionService = participantInstitutionService;
        this.studyService=studyService;
    }

    /**
     * {@code POST  /participant-institutions} : Create a new participantInstitution.
     *
     * @param participantInstitution the participantInstitution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new participantInstitution, or with status {@code 400 (Bad Request)} if the participantInstitution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/participant-institutions")//modificado
    public ResponseEntity<ParticipantInstitution> createParticipantInstitution(@RequestBody ParticipantInstitution participantInstitution) throws URISyntaxException {
        log.debug("REST request to save ParticipantInstitution : {}", participantInstitution);
        if (participantInstitution.getId() != null) {
            throw new BadRequestAlertException("A new participantInstitution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParticipantInstitution result = participantInstitutionService.save(participantInstitution);
        return ResponseEntity.created(new URI("/api/participant-institutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /participant-institutions} : Updates an existing participantInstitution.
     *
     * @param participantInstitution the participantInstitution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participantInstitution,
     * or with status {@code 400 (Bad Request)} if the participantInstitution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the participantInstitution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/participant-institutions")
    public ResponseEntity<ParticipantInstitution> updateParticipantInstitution(@RequestBody ParticipantInstitution participantInstitution) throws URISyntaxException {
        log.debug("REST request to update ParticipantInstitution : {}", participantInstitution);
        if (participantInstitution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParticipantInstitution result = participantInstitutionService.save(participantInstitution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, participantInstitution.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /participant-institutions} : get all the participantInstitutions.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of participantInstitutions in body.
     */
    @GetMapping("/participant-institutions")
    public ResponseEntity<List<ParticipantInstitution>> getAllParticipantInstitutions(Pageable pageable) {
        log.debug("REST request to get a page of ParticipantInstitutions");
        Page<ParticipantInstitution> page = participantInstitutionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /participant-institutions/:id} : get the "id" participantInstitution.
     *
     * @param id the id of the participantInstitution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the participantInstitution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/participant-institutions/{id}")
    public ResponseEntity<ParticipantInstitution> getParticipantInstitution(@PathVariable String id) {
        log.debug("REST request to get ParticipantInstitution : {}", id);
        Optional<ParticipantInstitution> participantInstitution = participantInstitutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(participantInstitution);
    }

    /**
     * {@code DELETE  /participant-institutions/:id} : delete the "id" participantInstitution.
     *
     * @param id the id of the participantInstitution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/participant-institutions/{id}")//modificado
    public ResponseEntity<Void> deleteParticipantInstitution(@PathVariable String id) {
        log.debug("REST request to delete ParticipantInstitution : {}", id);
        participantInstitutionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
