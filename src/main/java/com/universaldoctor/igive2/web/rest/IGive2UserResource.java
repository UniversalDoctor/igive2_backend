package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.domain.IGive2User;
import com.universaldoctor.igive2.service.IGive2UserService;
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
 * REST controller for managing {@link com.universaldoctor.igive2.domain.IGive2User}.
 */
@RestController
@RequestMapping("/api")
public class IGive2UserResource {

    private final Logger log = LoggerFactory.getLogger(IGive2UserResource.class);

    private static final String ENTITY_NAME = "iGive2User";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IGive2UserService iGive2UserService;

    public IGive2UserResource(IGive2UserService iGive2UserService) {
        this.iGive2UserService = iGive2UserService;
    }

    /**
     * {@code POST  /i-give-2-users} : Create a new iGive2User.
     *
     * @param iGive2User the iGive2User to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iGive2User, or with status {@code 400 (Bad Request)} if the iGive2User has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-give-2-users")
    public ResponseEntity<IGive2User> createIGive2User(@Valid @RequestBody IGive2User iGive2User) throws URISyntaxException {
        log.debug("REST request to save IGive2User : {}", iGive2User);
        if (iGive2User.getId() != null) {
            throw new BadRequestAlertException("A new iGive2User cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IGive2User result = iGive2UserService.save(iGive2User);
        return ResponseEntity.created(new URI("/api/i-give-2-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /i-give-2-users} : Updates an existing iGive2User.
     *
     * @param iGive2User the iGive2User to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iGive2User,
     * or with status {@code 400 (Bad Request)} if the iGive2User is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iGive2User couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-give-2-users")
    public ResponseEntity<IGive2User> updateIGive2User(@Valid @RequestBody IGive2User iGive2User) throws URISyntaxException {
        log.debug("REST request to update IGive2User : {}", iGive2User);
        if (iGive2User.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IGive2User result = iGive2UserService.save(iGive2User);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iGive2User.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /i-give-2-users} : get all the iGive2Users.
     *

     * @param pageable the pagination information.

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iGive2Users in body.
     */
    @GetMapping("/i-give-2-users")
    public ResponseEntity<List<IGive2User>> getAllIGive2Users(Pageable pageable, @RequestParam(required = false) String filter) {
        /*if ("researcher-is-null".equals(filter)) {
            log.debug("REST request to get all IGive2Users where researcher is null");
            return new ResponseEntity<>(iGive2UserService.findAllWhereResearcherIsNull(),
                    HttpStatus.OK);
        }*/
        log.debug("REST request to get a page of IGive2Users");
        Page<IGive2User> page = iGive2UserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /i-give-2-users/:id} : get the "id" iGive2User.
     *
     * @param id the id of the iGive2User to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iGive2User, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-give-2-users/{id}")
    public ResponseEntity<IGive2User> getIGive2User(@PathVariable String id) {
        log.debug("REST request to get IGive2User : {}", id);
        Optional<IGive2User> iGive2User = iGive2UserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iGive2User);
    }

    /**
     * {@code DELETE  /i-give-2-users/:id} : delete the "id" iGive2User.
     *
     * @param id the id of the iGive2User to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-give-2-users/{id}")
    public ResponseEntity<Void> deleteIGive2User(@PathVariable String id) {
        log.debug("REST request to delete IGive2User : {}", id);
        iGive2UserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
