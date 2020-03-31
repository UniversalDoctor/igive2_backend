package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.repository.StudyRepository;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.repository.ParticipantInvitationRepository;
import com.universaldoctor.igive2.service.dto.Invitations;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

/**
 * Service Implementation for managing {@link ParticipantInvitation}.
 */
@Service
public class ParticipantInvitationServiceImpl implements ParticipantInvitationService {

    private final Logger log = LoggerFactory.getLogger(ParticipantInvitationServiceImpl.class);

    private final ParticipantInvitationRepository participantInvitationRepository;


    private final StudyRepository studyRepository;
    private final ParticipantService participantService;
    private final UserService userService;

    public ParticipantInvitationServiceImpl(ParticipantInvitationRepository participantInvitationRepository,
                                            StudyRepository studyRepository, ParticipantService participantService, UserService userService) {
        this.participantInvitationRepository = participantInvitationRepository;
        this.studyRepository = studyRepository;
        this.participantService = participantService;
        this.userService = userService;
    }

    /**
     * Save a participantInvitation.
     *
     * @param participantInvitation the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ParticipantInvitation save(ParticipantInvitation participantInvitation) {
        log.debug("Request to save ParticipantInvitation : {}", participantInvitation);
        return participantInvitationRepository.save(participantInvitation);
    }

    /**
     * Get all the participantInvitations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<ParticipantInvitation> findAll(Pageable pageable) {
        log.debug("Request to get all ParticipantInvitations");
        return participantInvitationRepository.findAll(pageable);
    }


    /**
     * Get one participantInvitation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<ParticipantInvitation> findOne(String id) {
        log.debug("Request to get ParticipantInvitation : {}", id);
        return participantInvitationRepository.findById(id);
    }

    /**
     * Delete the participantInvitation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ParticipantInvitation : {}", id);
        participantInvitationRepository.deleteById(id);
    }

    /**
     * Delete the questions of form by id.
     *
     * @param invitation the entity.
     * @param researcher to check that institution belongs it.
     * @return true if it's all correctly.
     */
    @Override
    public void deleteInvitation(ParticipantInvitation invitation, Researcher researcher) {
        Optional<Study> study = studyRepository.findById(invitation.getStudy().getId());
        if (study.isPresent() && study.get().getResearcher().getId().equals(researcher.getId())) {
            if(invitation.isState()){
                Optional<Participant> participant=participantService.findOne(invitation.getParticipantId());
                if(participant.isPresent() && participant.get()!=null){
                    participantService.deleteParticipation(participant.get(),participant.get().getMobileUser());
                }
            }
            delete(invitation.getId());
        }
    }


    /**
     * create and associate a new invitation.
     *
     * @param studyId to associate.
     * @param researcher to check if the form belongs.
     * @param invitation the object.
     * @return the entity.
     */
    @Override
    public Optional<ParticipantInvitation> addAndSaveInvitation(String studyId, ParticipantInvitation invitation, Researcher researcher, User user) {
        Optional<Study> study = studyRepository.findById(studyId);
        if (study.isPresent() && study.get().getResearcher().getId().equals(researcher.getId())) {
            invitation.setState(false);
            save(invitation);
            invitation.setStudy(study.get());
            save(invitation);
        }
        if(study.get().getState() == State.PUBLISHED && study.get().isRecruiting()){
            userService.sendMailInvitation(user,study.get(),invitation.getEmail());
        }
        Optional<ParticipantInvitation> participantInvitation = findOne(invitation.getId());
        return participantInvitation;
    }

    /**
     * from a list of mails create and associate a  many new invitation.
     *
     * @param invitations to can get the mails, researcher and the study that i want send the invitation
     * @return the entitys.
     */
    @Override
    public ResponseEntity<Set<ParticipantInvitation>> addAndSaveManyInvitation(Invitations invitations) {
        Set<ParticipantInvitation> result=new HashSet<>();
        Optional<Study> study= studyRepository.findById(invitations.getStudyId());
        if(study.isPresent() && study.get().getState() == State.PUBLISHED ) {
            for (ParticipantInvitation invitation : invitations.getInvitations()) {
                Optional<ParticipantInvitation> object = participantInvitationRepository.findOneByEmailAndStudy(invitation.getEmail(), study.get());
                if (!object.isPresent() && !invitation.getEmail().equals(invitations.getUser().getEmail())) {
                    Optional<ParticipantInvitation> participant = addAndSaveInvitation(invitations.getStudyId(), invitation, invitations.getResearcher(),invitations.getUser());
                    if(participant.isPresent()){
                        result.add(participant.get());
                    }
                }
            }
            Optional<Set<ParticipantInvitation>> resultado = Optional.of(result);
            return ResponseUtil.wrapOrNotFound(resultado);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /*delete all invitations that they aren't accepted*/
    @Override
    public void deleteInvitationsNotAccepted(Study study) {
        Optional<Set<ParticipantInvitation>> invitations=participantInvitationRepository.findByStudyAndStateOrderByIdAsc(study,false);
        if(invitations.isPresent() && invitations.get()!=null){
            Iterator<ParticipantInvitation> iterator=invitations.get().iterator();
            while (iterator.hasNext()){
                deleteInvitation(iterator.next(),study.getResearcher());
            }
        }
    }

    /*get the invitations that have this email and belong at this study*/
    @Override
    public Optional<ParticipantInvitation> getFromEmailAndStudy(String email, Study study) {
        return participantInvitationRepository.findOneByEmailAndStudy(email,study);
    }

    /*get all the invitations that have this study*/
    @Override
    public Optional<Set<ParticipantInvitation>> getFromStudy(Study study) {
        return participantInvitationRepository.findByStudyOrderByIdAsc(study);
    }
}
