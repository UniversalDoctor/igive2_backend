package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.domain.FormAnswers;
import com.universaldoctor.igive2.service.FormAnswersService;
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
 * REST controller for managing {@link com.universaldoctor.igive2.domain.FormAnswers}.
 */
@RestController
@RequestMapping("/api")
public class FormAnswersResource {

    private final Logger log = LoggerFactory.getLogger(FormAnswersResource.class);

    private static final String ENTITY_NAME = "formAnswers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormAnswersService formAnswersService;

    public FormAnswersResource(FormAnswersService formAnswersService) {
        this.formAnswersService = formAnswersService;
    }

    /**
     * {@code POST  /form-answers} : Create a new formAnswers.
     *
     * @param formAnswers the formAnswers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formAnswers, or with status {@code 400 (Bad Request)} if the formAnswers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-answers")
    public ResponseEntity<FormAnswers> createFormAnswers(@RequestBody FormAnswers formAnswers) throws URISyntaxException {
        log.debug("REST request to save FormAnswers : {}", formAnswers);
        if (formAnswers.getId() != null) {
            throw new BadRequestAlertException("A new formAnswers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormAnswers result = formAnswersService.save(formAnswers);
        return ResponseEntity.created(new URI("/api/form-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /form-answers} : Updates an existing formAnswers.
     *
     * @param formAnswers the formAnswers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formAnswers,
     * or with status {@code 400 (Bad Request)} if the formAnswers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formAnswers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/form-answers")
    public ResponseEntity<FormAnswers> updateFormAnswers(@RequestBody FormAnswers formAnswers) throws URISyntaxException {
        log.debug("REST request to update FormAnswers : {}", formAnswers);
        if (formAnswers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormAnswers result = formAnswersService.save(formAnswers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formAnswers.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /form-answers} : get all the formAnswers.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formAnswers in body.
     */
    @GetMapping("/form-answers")
    public ResponseEntity<List<FormAnswers>> getAllFormAnswers(Pageable pageable) {
        log.debug("REST request to get a page of FormAnswers");
        Page<FormAnswers> page = formAnswersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /form-answers/:id} : get the "id" formAnswers.
     *
     * @param id the id of the formAnswers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formAnswers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/form-answers/{id}")
    public ResponseEntity<FormAnswers> getFormAnswers(@PathVariable String id) {
        log.debug("REST request to get FormAnswers : {}", id);
        Optional<FormAnswers> formAnswers = formAnswersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formAnswers);
    }

    /**
     * {@code DELETE  /form-answers/:id} : delete the "id" formAnswers.
     *
     * @param id the id of the formAnswers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/form-answers/{id}")
    public ResponseEntity<Void> deleteFormAnswers(@PathVariable String id) {
        log.debug("REST request to delete FormAnswers : {}", id);
        formAnswersService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
