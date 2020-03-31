package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.IGive2User;
import com.universaldoctor.igive2.domain.MobileUser;

import com.universaldoctor.igive2.domain.Participant;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.service.dto.Profile;
import com.universaldoctor.igive2.service.dto.SetUp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link MobileUser}.
 */
public interface MobileUserService {

    /**
     * Save a mobileUser.
     *
     * @param mobileUser the entity to save.
     * @return the persisted entity.
     */
    MobileUser save(MobileUser mobileUser);

    /**
     * Get all the mobileUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MobileUser> findAll(Pageable pageable);


    /**
     * Get the "id" mobileUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MobileUser> findOne(String id);

    /**
     * Delete the "id" mobileUser.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Delete the mobileUser.
     *
     * @param mobileUser the entity.
     */
    void deleteProfile(MobileUser mobileUser);

    /**
     * update the mobileUser.
     * @param mobileUser the original.
     * @param mobileUser the change.
     * @return the entity.
     */
    Optional<MobileUser> update(MobileUser user,MobileUser mobileUser);

    /**
     * create a MobileUser.
     *
     * @param iGive2User the entity to associate.
     * @param userId  to associate.
     * @return the persisted entity.
     */
    MobileUser create(IGive2User iGive2User, String userId);

    /**
     * update  MobileUser, igive2user, and create data weight and height .
     *
     * @param mobileUser the entity.
     * @param setUp the change the information.
     * @return the persisted entity.
     */
    void setUp(MobileUser mobileUser,SetUp setUp);

    /**
     *  get the mobile profile with setup form .
     *
     * @param mobileUser the entity.
     * @return mobile profile with setup form.
     */
    SetUp getSetUp(MobileUser mobileUser);

    /**
     * update profile of mobileuser (profile= username, icon, status)
     *
     * @param mobileUser the entity.
     * @param profile the change the information.
     */
    void setProfile(MobileUser mobileUser, Profile profile);

    /**
     *  get profile of mobileuser (profile= username, icon, status)
     *
     * @param mobileUser the entity to get the information.
     * @return (profile= username, icon, status)
     */
    Profile getProfile(MobileUser mobileUser);
}
