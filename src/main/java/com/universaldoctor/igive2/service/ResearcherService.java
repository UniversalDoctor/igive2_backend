package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.*;

import com.universaldoctor.igive2.service.dto.DashboardProfile;
import com.universaldoctor.igive2.service.dto.MailDTO;
import com.universaldoctor.igive2.service.dto.ManagedUserVM;
import com.universaldoctor.igive2.service.dto.ResearcherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link Researcher}.
 */
public interface ResearcherService {

    /**
     * Save a researcher.
     *
     * @param researcher the entity to save.
     * @return the persisted entity.
     */
    Researcher save(Researcher researcher);

    /**
     * Get all the researchers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Researcher> findAll(Pageable pageable);


    /**
     * Get the "id" researcher.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Researcher> findOne(String id);

    /**
     * Delete the "id" researcher.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Get the profile of researcher with information of user and igive2user too.
     *
     * @param researcher the object to get the information.
     * @return the entity.
     */
    DashboardProfile getResearcher(Researcher researcher);

    /**
     * Delete the entity researcher.
     *
     * @param researcher the entity.
     */
    void deleteResearcher(Researcher researcher);

    /**
     * Update the profile of researcher with information of user and igive2user too.
     *
     * @param dashboardProfile the object to get the information that I want update.
     * @param id of the researcher for can updated it
     * @return the entity.
     */
    DashboardProfile setResearcher(DashboardProfile dashboardProfile,String id);

    /**
     * Sen an email to all participantsç
     *
     * @param mail the object to get the information of participants, subject and body of mail
     *
     */
    void sendMailAllParticipants(MailDTO mail);

    /*From a list of invitations that are accepted returns an array with the emails of participants*/
    String[] getDestinatarios(Set<ParticipantInvitation> participantInvitation);


    /*from ResearcherDTO create and returns a ManagedUserVM , is the information that you need for the registration*/
    ManagedUserVM passOfResearcherDTOtoManagedUser(ResearcherDTO researcherDTO);

    /*create and save a researcher with the features institution, honorifics, userId and iGive2User*/
    Researcher create(String institution,String honorifics, String userId, IGive2User iGive2User);
}
