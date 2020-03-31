package com.universaldoctor.igive2.web.rest;

import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.service.MobileUserService;
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
 * REST controller for managing {@link com.universaldoctor.igive2.domain.MobileUser}.
 */
@RestController
@RequestMapping("/api")
public class MobileUserResource {

    private final Logger log = LoggerFactory.getLogger(MobileUserResource.class);

    private static final String ENTITY_NAME = "mobileUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MobileUserService mobileUserService;

    public MobileUserResource(MobileUserService mobileUserService) {
        this.mobileUserService = mobileUserService;
    }

    /**
     * {@code POST  /mobile-users} : Create a new mobileUser.
     *
     * @param mobileUser the mobileUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mobileUser, or with status {@code 400 (Bad Request)} if the mobileUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mobile-users")
    public ResponseEntity<MobileUser> createMobileUser(@Valid @RequestBody MobileUser mobileUser) throws URISyntaxException {
        log.debug("REST request to save MobileUser : {}", mobileUser);
        if (mobileUser.getId() != null) {
            throw new BadRequestAlertException("A new mobileUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MobileUser result = mobileUserService.save(mobileUser);
        return ResponseEntity.created(new URI("/api/mobile-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mobile-users} : Updates an existing mobileUser.
     *
     * @param mobileUser the mobileUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mobileUser,
     * or with status {@code 400 (Bad Request)} if the mobileUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mobileUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mobile-users")
    public ResponseEntity<MobileUser> updateMobileUser(@Valid @RequestBody MobileUser mobileUser) throws URISyntaxException {
        log.debug("REST request to update MobileUser : {}", mobileUser);
        if (mobileUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MobileUser result = mobileUserService.save(mobileUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mobileUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mobile-users} : get all the mobileUsers.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mobileUsers in body.
     */
    @GetMapping("/mobile-users")
    public ResponseEntity<List<MobileUser>> getAllMobileUsers(Pageable pageable) {
        log.debug("REST request to get a page of MobileUsers");
        Page<MobileUser> page = mobileUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mobile-users/:id} : get the "id" mobileUser.
     *
     * @param id the id of the mobileUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mobileUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mobile-users/{id}")
    public ResponseEntity<MobileUser> getMobileUser(@PathVariable String id) {
        log.debug("REST request to get MobileUser : {}", id);
        Optional<MobileUser> mobileUser = mobileUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mobileUser);
    }

    /**
     * {@code DELETE  /mobile-users/:id} : delete the "id" mobileUser.
     *
     * @param id the id of the mobileUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mobile-users/{id}")
    public ResponseEntity<Void> deleteMobileUser(@PathVariable String id) {
        log.debug("REST request to delete MobileUser : {}", id);
        mobileUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
