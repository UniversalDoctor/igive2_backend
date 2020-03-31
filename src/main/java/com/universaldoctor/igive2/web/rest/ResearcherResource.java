package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.service.ResearcherService;
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

/**
 * REST controller for managing {@link com.universaldoctor.igive2.domain.Researcher}.
 */
@RestController
@RequestMapping("/api")
public class ResearcherResource {

    private final Logger log = LoggerFactory.getLogger(ResearcherResource.class);

    private static final String ENTITY_NAME = "researcher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResearcherService researcherService;

    public ResearcherResource(ResearcherService researcherService) {
        this.researcherService = researcherService;
    }

    /**
     * {@code POST  /researchers} : Create a new researcher.
     *
     * @param researcher the researcher to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new researcher, or with status {@code 400 (Bad Request)} if the researcher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/researchers")
    public ResponseEntity<Researcher> createResearcher(@Valid @RequestBody Researcher researcher) throws URISyntaxException {
        log.debug("REST request to save Researcher : {}", researcher);
        if (researcher.getId() != null) {
            throw new BadRequestAlertException("A new researcher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Researcher result = researcherService.save(researcher);
        return ResponseEntity.created(new URI("/api/researchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /researchers} : Updates an existing researcher.
     *
     * @param researcher the researcher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated researcher,
     * or with status {@code 400 (Bad Request)} if the researcher is not valid,
     * or with status {@code 500 (Internal Server Error)} if the researcher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/researchers")
    public ResponseEntity<Researcher> updateResearcher(@Valid @RequestBody Researcher researcher) throws URISyntaxException {
        log.debug("REST request to update Researcher : {}", researcher);
        if (researcher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Researcher result = researcherService.save(researcher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, researcher.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /researchers} : get all the researchers.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of researchers in body.
     */
    @GetMapping("/researchers")
    public ResponseEntity<List<Researcher>> getAllResearchers(Pageable pageable) {
        log.debug("REST request to get a page of Researchers");
        Page<Researcher> page = researcherService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /researchers/:id} : get the "id" researcher.
     *
     * @param id the id of the researcher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the researcher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/researchers/{id}")
    public ResponseEntity<Researcher> getResearcher(@PathVariable String id) {
        log.debug("REST request to get Researcher : {}", id);
        Optional<Researcher> researcher = researcherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(researcher);
    }

    /**
     * {@code DELETE  /researchers/:id} : delete the "id" researcher.
     *
     * @param id the id of the researcher to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/researchers/{id}")
    public ResponseEntity<Void> deleteResearcher(@PathVariable String id) {
        log.debug("REST request to delete Researcher : {}", id);
        researcherService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
