package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.Form;
import com.universaldoctor.igive2.domain.ParticipantInstitution;

import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link ParticipantInstitution}.
 */
public interface ParticipantInstitutionService {

    /**
     * Save a participantInstitution.
     *
     * @param participantInstitution the entity to save.
     * @return the persisted entity.
     */
    ParticipantInstitution save(ParticipantInstitution participantInstitution);

    /**
     * Get all the participantInstitutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParticipantInstitution> findAll(Pageable pageable);


    /**
     * Get the "id" participantInstitution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParticipantInstitution> findOne(String id);

    /**
     * Delete the "id" participantInstitution.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Delete the "institution" of a study .
     *
     * @param institutionId the id of the entity.
     * @param researcher to check if the form belongs it .
     */

    void  deleteInstitution(String institutionId, Researcher researcher);

    /**
     * create and associate a new institution.
     *
     * @param studyId to associate.
     * @param researcher to check if the form belongs.
     * @param institution the object.
     * @return the entity.
     */
    void addAndSaveInstitution(String studyId,ParticipantInstitution institution,Researcher researcher);

    /*return all institutions of one study*/
    Optional<Set<ParticipantInstitution>> getInstitutions(Study study);

    /* from study get all institutions that belong it, and then delete all*/
    void deleteAllIntitutionsOfStudy (Study study);
}
