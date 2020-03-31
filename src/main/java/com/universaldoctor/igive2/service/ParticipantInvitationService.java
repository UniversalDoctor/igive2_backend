package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.*;

import com.universaldoctor.igive2.service.dto.Invitations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link ParticipantInvitation}.
 */
public interface ParticipantInvitationService {

    /**
     * Save a participantInvitation.
     *
     * @param participantInvitation the entity to save.
     * @return the persisted entity.
     */
    ParticipantInvitation save(ParticipantInvitation participantInvitation);

    /**
     * Get all the participantInvitations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParticipantInvitation> findAll(Pageable pageable);


    /**
     * Get the "id" participantInvitation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParticipantInvitation> findOne(String id);

    /**
     * Delete the "id" participantInvitation.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Delete the "institution" of a study .
     *
     * @param invitation the entity.
     * @param researcher to check if the form belongs it .
     */

    void deleteInvitation(ParticipantInvitation invitation, Researcher researcher);

    /**
     * create and associate a new invitation.
     *
     * @param studyId to associate.
     * @param researcher to check if the form belongs.
     * @param invitation the object.
     * @return the entity.
     */
    Optional<ParticipantInvitation> addAndSaveInvitation(String studyId,ParticipantInvitation invitation,Researcher researcher,User user);


    /**
     * from a list of mails create and associate a  many new invitation.
     *
     * @param invitations to can get the mails, researcher and the study that i want send the invitation
     * invitations contains user to check if the researcher try to invite himself and is needed for send email to invitations
     * @return list of the entity.
     */
    ResponseEntity<Set<ParticipantInvitation>> addAndSaveManyInvitation(Invitations invitations);

    /*delete all invitations that they aren't accepted*/
    void deleteInvitationsNotAccepted(Study study);

    /*get the invitations that have this email and belong at this study*/
    Optional<ParticipantInvitation> getFromEmailAndStudy(String email,Study study);

    /*get all the invitations that have this study*/
    Optional<Set<ParticipantInvitation>> getFromStudy(Study study);

}
