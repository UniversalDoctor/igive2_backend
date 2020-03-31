package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.domain.FormQuestion;
import com.universaldoctor.igive2.service.FormQuestionService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.universaldoctor.igive2.domain.FormQuestion}.
 */
@RestController
@RequestMapping("/api")
public class FormQuestionResource {

    private final Logger log = LoggerFactory.getLogger(FormQuestionResource.class);

    private static final String ENTITY_NAME = "formQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormQuestionService formQuestionService;

    public FormQuestionResource(FormQuestionService formQuestionService) {
        this.formQuestionService = formQuestionService;
    }

    /**
     * {@code POST  /form-questions} : Create a new formQuestion.
     *
     * @param formQuestion the formQuestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formQuestion, or with status {@code 400 (Bad Request)} if the formQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-questions")
    public ResponseEntity<FormQuestion> createFormQuestion(@Valid @RequestBody FormQuestion formQuestion) throws URISyntaxException {
        log.debug("REST request to save FormQuestion : {}", formQuestion);
        if (formQuestion.getId() != null) {
            throw new BadRequestAlertException("A new formQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormQuestion result = formQuestionService.save(formQuestion);
        return ResponseEntity.created(new URI("/api/form-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /form-questions} : Updates an existing formQuestion.
     *
     * @param formQuestion the formQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formQuestion,
     * or with status {@code 400 (Bad Request)} if the formQuestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/form-questions")
    public ResponseEntity<FormQuestion> updateFormQuestion(@Valid @RequestBody FormQuestion formQuestion) throws URISyntaxException {
        log.debug("REST request to update FormQuestion : {}", formQuestion);
        if (formQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormQuestion result = formQuestionService.save(formQuestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formQuestion.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /form-questions} : get all the formQuestions.
     *

     * @param pageable the pagination information.

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formQuestions in body.
     */
    @GetMapping("/form-questions")
    public ResponseEntity<List<FormQuestion>> getAllFormQuestions(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("answer-is-null".equals(filter)) {
            log.debug("REST request to get all FormQuestions where answer is null");
            return new ResponseEntity<>(formQuestionService.findAllWhereAnswerIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of FormQuestions");
        Page<FormQuestion> page = formQuestionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /form-questions/:id} : get the "id" formQuestion.
     *
     * @param id the id of the formQuestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formQuestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/form-questions/{id}")
    public ResponseEntity<FormQuestion> getFormQuestion(@PathVariable String id) {
        log.debug("REST request to get FormQuestion : {}", id);
        Optional<FormQuestion> formQuestion = formQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formQuestion);
    }

    /**
     * {@code DELETE  /form-questions/:id} : delete the "id" formQuestion.
     *
     * @param id the id of the formQuestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/form-questions/{id}")
    public ResponseEntity<Void> deleteFormQuestion(@PathVariable String id) {
        log.debug("REST request to delete FormQuestion : {}", id);
        formQuestionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
