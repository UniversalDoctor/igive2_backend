package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.service.FormAnswersService;
import com.universaldoctor.igive2.service.ParticipantService;
import com.universaldoctor.igive2.service.dto.ParticipantDTO;
import com.universaldoctor.igive2.web.rest.ParticipantResource;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

/**
 * Service Implementation for managing {@link Participant}.
 */
@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final Logger log = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    private final ParticipantRepository participantRepository;

    private final MobileUserRepository mobileUserRepository;
    private final UserRepository userRepository;
    private final ParticipantInvitationRepository participantInvitationRepository;
    private final StudyRepository studyRepository;
    private final FormAnswersService formAnswersService;

    public ParticipantServiceImpl(ParticipantRepository participantRepository, MobileUserRepository mobileUserRepository, UserRepository userRepository, ParticipantInvitationRepository participantInvitationRepository, StudyRepository studyRepository, FormAnswersService formAnswersService) {
        this.participantRepository = participantRepository;
        this.mobileUserRepository = mobileUserRepository;
        this.userRepository = userRepository;
        this.participantInvitationRepository = participantInvitationRepository;
        this.studyRepository = studyRepository;
        this.formAnswersService = formAnswersService;
    }

    /**
     * Save a participant.
     *
     * @param participant the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Participant save(Participant participant) {
        log.debug("Request to save Participant : {}", participant);
        return participantRepository.save(participant);
    }

    /**
     * Get all the participants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Participant> findAll(Pageable pageable) {
        log.debug("Request to get all Participants");
        return participantRepository.findAll(pageable);
    }


    /**
     * Get one participant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Participant> findOne(String id) {
        log.debug("Request to get Participant : {}", id);
        return participantRepository.findById(id);
    }

    /**
     * Delete the participant by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Participant : {}", id);
        participantRepository.deleteById(id);
    }

    /**
     * create a participant.
     *
     * @param mobileUser the entity to associate.
     * @param study the entity to associate.
     * @param invitation for check if this mobileuser have an invitation and if the mobile user have an invitation, it pass an active state
     * @return the persisted entity.
     */
    @Override
    public Participant create(MobileUser mobileUser, Study study, ParticipantInvitation invitation) {
        String anonymousId =(String) UUID.randomUUID().toString().subSequence(0, 8);
        Participant participant = new Participant();
        participant.setMobileUser(mobileUser);
        participant.setStudy(study);
        participant.setEntryDate(Instant.now());
        participant.setAnonymousId(anonymousId);
        save(participant);
        invitation.setState(true);
        invitation.setParticipantId(participant.getId());
        participantInvitationRepository.save(invitation);
        return participant;
    }

    /**
     * delete a participant.
     *
     * @param  participant the entity that I want delete
     * @param mobileUser for check if the participant belong it and delete association.
     * @return true if all it's sucessfull.
     */
    @Override
    public ResponseEntity<Boolean> deleteParticipation(Participant participant, MobileUser mobileUser) {
        Optional<User> user=userRepository.findById(mobileUser.getUserId());
        Optional<Study> study= Optional.ofNullable(participant.getStudy());
        if(participant.getMobileUser().getId().equals(mobileUser.getId())) {
            Optional<Set<FormAnswers>> formAnswers= formAnswersService.getFromParticipan(participant);
            if(formAnswers.isPresent()){
                formAnswersService.deleteAllFormAnswer(participant);
            }
            if(user.isPresent() && study.isPresent()) {
                Optional<ParticipantInvitation> invitation = participantInvitationRepository.findOneByEmailAndStudy(user.get().getEmail(),study.get());
                if(invitation.isPresent()){
                    participantInvitationRepository.delete(invitation.get());
                }
            }
            delete(participant.getId());
            return new ResponseEntity(true, HttpStatus.OK);
        }
        ResponseEntity error=new ResponseEntity(false, HttpStatus.CONFLICT);
        return error;
    }

    /* i need the list  the participants that it have the mobile user. Return a list of participants but with
    reduced information, the participant don't show the mobile user neither the study */
    @Override
    public ArrayList<ParticipantDTO> getParticipantsDTO(Set<Participant> participants) {
        Iterator<Participant> iterator=participants.iterator();
        ArrayList<ParticipantDTO> dtos=new ArrayList<>();
        while (iterator.hasNext()){
            Optional<ParticipantDTO> object= Optional.ofNullable(createDTO(iterator.next()));
            if (object.isPresent()){
                dtos.add(object.get());
            }
        }
        return dtos;
    }
    /*from a participant return the same participant but it don't show the mobile user neither the study*/
    @Override
    public ParticipantDTO createDTO(Participant participant) {
        Optional<Study> study=studyRepository.findById(participant.getStudy().getId());
        if(study.isPresent()) {
            ParticipantDTO participantDTO = new ParticipantDTO(participant.getId(), participant.getAnonymousId(),
                participant.getEntryDate(), participant.getStudy().getId(),!participant.getParticipantData().isEmpty(), participant.getStudy().getCode(),
                participant.getStudy().getName(), participant.getStudy().getDescription(),participant.getStudy().getDataJustification(), participant.getStudy().getState(),
                participant.getStudy().getStartDate(), participant.getStudy().getEndDate(),participant.getStudy().getIcon());
            return participantDTO;
        }
        return null;
    }

    /**
     * delete all participant.
     *
     * @param mobileUser need for search all participants of it and all formsanswers.
     */
    @Override
    public void deleteAllParticipation(MobileUser mobileUser) {
        Optional<User> user= userRepository.findById(mobileUser.getUserId());
        Optional<Set<Participant>> participants= participantRepository.findByMobileUserOrderByEntryDateAsc(mobileUser);
        if(participants.isPresent() && user.isPresent()){
            Iterator<Participant> iterator= participants.get().iterator();
            while (iterator.hasNext()){
              Participant object=iterator.next();
              formAnswersService.deleteAllFormAnswer(object);
              Optional<ParticipantInvitation> invitation= participantInvitationRepository.findOneByEmailAndStudy(user.get().getEmail(),object.getStudy());
              if(invitation.isPresent() && invitation.get()!=null){
                  participantInvitationRepository.delete(invitation.get());
              }
              delete(object.getId());
            }
        }
    }

    /**
     * Get the Participant object from the Study and the mobile user
     * @param mobileUser the mobile user of participant
     * @param study the study containing the participant
     * @return the entity
     */
    public Optional<Participant> getParticipantFromMobileAndStudy(MobileUser mobileUser, Study study){
        return participantRepository.findOneByStudyAndMobileUserOrderByEntryDateAsc(study, mobileUser);
    }
    /**
     * Get all the Participant object from the mobile user
     * @param mobileUser the mobile user of participants
     * @return a list of the entity
     */
    @Override
    public Optional<Set<Participant>> getParticipants(MobileUser mobileUser) {
        return participantRepository.findByMobileUserOrderByEntryDateAsc(mobileUser);
    }

    /**
     * Get all the Participant object from the study
     * @param study the study where it participate
     * @return a list of the entity
     */
    @Override
    public Optional<Set<Participant>> getFromStudy(Study study) {
        return participantRepository.findByStudyOrderByEntryDateAsc(study);
    }

    /**
     * Get all the Participant object from the mobileuser
     * @param mobileUser the mobileuser that it belongs
     * @return a list of the entity
     */
    @Override
    public Optional<Set<Participant>> getFromMobileUser(MobileUser mobileUser) {
        return participantRepository.findByMobileUserOrderByEntryDateAsc(mobileUser);
    }
}
