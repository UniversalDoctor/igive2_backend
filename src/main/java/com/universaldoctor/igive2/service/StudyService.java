package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.Study;

import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.service.dto.StudyDTO;
import com.universaldoctor.igive2.service.dto.StudyManage;
import com.universaldoctor.igive2.service.dto.StudyManageMobile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link Study}.
 */
public interface StudyService {

    /**
     * Save a study.
     *
     * @param study the entity to save.
     * @return the persisted entity.
     */
    Study save(Study study);

    /**
     * Get all the studies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Study> findAll(Pageable pageable);


    /**
     * Get the "id" study.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Study> findOne(String id);

    /**
     * Delete the "id" study.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Get if the mobileuser is alredy in a study.
     *
     * @param mobileUser to check if it have a participation of study.
     * @param studyCode to get study .
     * @return true if the mobileuser have already a participation in the study .
     */
    boolean alreadyParticipate(MobileUser mobileUser, String studyCode);

    /**
     * Delete the study, (and all his intistituions, forms and invitations)
     *
     * @param study the id of the entity that i want delete.
     * @param researcher to check if the form belongs it .
     * @return true if it's sucessfull
     */

     boolean deleteStudy(Study study, Researcher researcher);

    /**
     * create and associate a new Study.
     *
     * @param researcher to associate.
     * @param study the object.
     * @return the entity.
     */
    Optional<Study> addAndSaveStudy(Study study,Researcher researcher);

    /**
     * update Study.
     *
     * @param original to update.
     * @param study the object.
     * @return the entity updated.
     */
    Optional<Study> updateStudy(Study study,Study original);

    /**
     * check if the studycode is alredy used.
     *
     * @param studyCode to check if another study use this code.
     * @return true if the studyCode have already in use .
     */
    boolean alreadyCodeUsed(String studyCode);

    /**
     * create a studyDTO.
     *
     * @param study info need for create.
     * @return the study with reduced information.
     */
    StudyDTO getDTO(Study study);

    /**
     * create a list of studyDTO.
     *
     * @param studies for get all studies.
     * @return list of entity.
     */
    ArrayList<StudyDTO> getStudiesDTO(Set<Study> studies);

    /**
     * get invitations of studies are accepted.
     *
     * @param study to get invitations.
     * @return list of the invitations are accepted .
     */
    Optional<Set<ParticipantInvitation>> getInvitationsAccepted(Study study);

    /**
     * return  number of studies are in this state.
     * @param state the state that i want check
     * @param researcher info need for check all studies.
     * @return the number.
     */
    int getStateStudies(Researcher researcher,State state);

    /**from study get the information of it and his participants with the mean of her health
     *  data and the % of forms have been answered
     *
     * @param study for get info of it and his participants
     * @return the entity.
     */
    StudyManage createStudyManage(Study study);

    /**from study get the information of it and his forms with only title and id of it
     *
     * @param study for get info of it and his forms
     * @return the entity.
     */
    StudyManageMobile createStudyManageMobile(Study study,MobileUser mobileUser);

    /**
     * get one study
     *
     * @param code the code of study
     * @return the entity.
     */
    Optional<Study> getStudyByCode(String code);

    /**
     * get one study
     *
     * @param researcher to check if the studies belong to it
     * @return a list of entity.
     */
    Optional<Set<Study>> getStudyByResearcher(Researcher researcher);

    /**
     * get one study
     *
     * @param researcher to check if the studies belong to it
     * @param state for check if the studies be in it state
     * @return a list of entity.
     */
    Optional<Set<Study>> getStudyByResearcherAndState(Researcher researcher, State state);


}
