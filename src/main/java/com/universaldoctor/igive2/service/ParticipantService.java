package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.Participant;

import com.universaldoctor.igive2.domain.ParticipantInvitation;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.service.dto.StudyParticipantManage;
import com.universaldoctor.igive2.service.dto.ParticipantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link Participant}.
 */
public interface ParticipantService {

    /**
     * Save a participant.
     *
     * @param participant the entity to save.
     * @return the persisted entity.
     */
    Participant save(Participant participant);

    /**
     * Get all the participants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Participant> findAll(Pageable pageable);


    /**
     * Get the "id" participant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Participant> findOne(String id);

    /**
     * Delete the "id" participant.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * create a participant.
     *
     * @param mobileUser the entity to associate.
     * @param study the entity to associate
     * @param invitation for check if this mobileuser have an invitation and if the mobile user have an invitation, it pass an active state
     * @return the persisted entity.
     */
    Participant create(MobileUser mobileUser, Study study, ParticipantInvitation invitation);

    /**
     * delete a participant.
     *
     * @param  participant the entity that I want delete
     * @param mobileUser for check if the participant belong it and delete association.
     * @return true if all it's sucessfull.
     */
    ResponseEntity<Boolean> deleteParticipation(Participant participant, MobileUser mobileUser);

    /* i need the list  the participants that it have the mobile user. Return a list of participants but with
    reduced information, the participant don't show the mobile user neither the study */
    ArrayList<ParticipantDTO> getParticipantsDTO(Set<Participant> participants);

    /*from a participant return the same participant but it don't show the mobile user neither the study*/
    ParticipantDTO createDTO(Participant participant);

    /**
     * delete all participant.
     *
     * @param mobileUser need for chach all participants of it.
     */
    void deleteAllParticipation(MobileUser mobileUser);

    /**
     * Get the Participant object from the Study and the mobile user
     * @param mobileUser the mobile user of participant
     * @param study the study containing the participant
     * @return the entity
     */
    Optional<Participant> getParticipantFromMobileAndStudy(MobileUser mobileUser, Study study);

    /**
     * Get all the Participant object from the mobile user
     * @param mobileUser the mobile user of participants
     * @return a list of the entity
     */
    Optional<Set<Participant>> getParticipants(MobileUser mobileUser);

    /**
     * Get all the Participant object from the study
     * @param study the study where it participate
     * @return a list of the entity
     */
    Optional<Set<Participant>> getFromStudy(Study study);

    /**
     * Get all the Participant object from the mobileuser
     * @param mobileUser the mobileuser that it belongs
     * @return a list of the entity
     */
    Optional<Set<Participant>> getFromMobileUser(MobileUser mobileUser);
}
