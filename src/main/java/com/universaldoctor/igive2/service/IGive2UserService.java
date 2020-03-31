package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.Igive2App;
import com.universaldoctor.igive2.domain.IGive2User;

import com.universaldoctor.igive2.domain.MobileUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link IGive2User}.
 */
public interface IGive2UserService {

    /**
     * Save a iGive2User.
     *
     * @param iGive2User the entity to save.
     * @return the persisted entity.
     */
    IGive2User save(IGive2User iGive2User);

    /**
     * Get all the iGive2Users.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IGive2User> findAll(Pageable pageable);

    /**
     * Get all the IGive2UserDTO where Researcher is {@code null}.
     *
     * @return the list of entities.
     */
   // List<IGive2User> findAllWhereResearcherIsNull();


    /**
     * Get the "id" iGive2User.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IGive2User> findOne(String id);

    /**
     * Delete the "id" iGive2User.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * create a Igive2user.
     *
     * @param newsLetter part of body.
     * @param country part of body.
     * @return the persisted entity.
     */
    IGive2User create(boolean newsLetter,String country);


}
