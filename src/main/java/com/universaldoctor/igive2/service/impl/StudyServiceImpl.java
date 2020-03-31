package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.StudyService;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.service.dto.*;
import com.universaldoctor.igive2.repository.StudyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

/**
 * Service Implementation for managing {@link Study}.
 */
@Service
public class StudyServiceImpl implements StudyService {

    private final Logger log = LoggerFactory.getLogger(StudyServiceImpl.class);

    private final StudyRepository studyRepository;

    private final ResearcherRepository researcherRepository;
    private final FormRepository formRepository;
    private final DataService dataService;
    private final ParticipantRepository participantRepository;
    private final FormService formService;
    private final FormAnswersService formAnswersService;
    private final ParticipantInvitationService participantInvitationService;
    private final ParticipantInvitationRepository participantInvitationRepository;
    private final ParticipantInstitutionService participantInstitutionService;

    public StudyServiceImpl(StudyRepository studyRepository, ResearcherRepository researcherRepository, FormRepository formRepository, DataService dataService, ParticipantRepository participantRepository, FormService formService,
                            FormAnswersService formAnswersService, ParticipantInvitationService participantInvitationService, ParticipantInvitationRepository participantInvitationRepository, ParticipantInstitutionService participantInstitutionService) {
        this.studyRepository = studyRepository;
        this.researcherRepository = researcherRepository;
        this.formRepository = formRepository;
        this.dataService = dataService;
        this.participantRepository = participantRepository;
        this.formService = formService;
        this.formAnswersService = formAnswersService;
        this.participantInvitationService = participantInvitationService;
        this.participantInvitationRepository = participantInvitationRepository;
        this.participantInstitutionService = participantInstitutionService;
    }

    /**
     * Save a study.
     *
     * @param study the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Study save(Study study) {
        log.debug("Request to save Study : {}", study);
        return studyRepository.save(study);
    }

    /**
     * Get all the studies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Study> findAll(Pageable pageable) {
        log.debug("Request to get all Studies");
        return studyRepository.findAll(pageable);
    }


    /**
     * Get one study by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Study> findOne(String id) {
        log.debug("Request to get Study : {}", id);
        return studyRepository.findById(id);
    }

    /**
     * Delete the study by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Study : {}", id);
        studyRepository.deleteById(id);
    }

    /**
     * Get if the mobileuser is alredy in a study.
     *
     * @param mobileUser to check if it have a participation of study.
     * @param studyCode to get study .
     * @return true if the mobileuser have already a participation in the study .
     */
    @Override
    public boolean alreadyParticipate(MobileUser mobileUser, String studyCode) {
        Optional<Set<Participant>> participants = participantRepository.findByMobileUserOrderByEntryDateAsc(mobileUser);
        if(participants.isPresent()){
            Iterator<Participant> iterator=participants.get().iterator();
            while (iterator.hasNext()){
                Participant object= iterator.next();
                if(object.getStudy().getCode().equals(studyCode) && object!=null){
                    log.debug(" no creo un nuevo participante ya que hay un participante con este estudio");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Delete the study, (and all his intistituions, forms and invitations)
     *
     * @param study the id of the entity that i want delete.
     * @param researcher to check if the form belongs it .
     * @return true if it's sucessfull
     */
    @Override
    public boolean deleteStudy(Study study, Researcher researcher) {;
        if(study.getResearcher().getId().equals(researcher.getId())){
            participantInstitutionService.deleteAllIntitutionsOfStudy(study);
            Optional<Set<Form>> forms=formRepository.findByStudyOrderByNameDesc(study);
            log.debug("\n\nforms of study: "+forms.get()+"\n");
            if(forms.isPresent() && forms.get()!=null) {
                if (!forms.get().isEmpty()) {
                    Iterator<Form> iterator = forms.get().iterator();
                    while (iterator.hasNext()) {
                        formService.deleteForm(iterator.next().getId(), researcher);
                    }
                }
            }
            Optional<Set<ParticipantInvitation>> invitations=participantInvitationRepository.findByStudyOrderByIdAsc(study);
            log.debug("\n\ninvitations of study: "+invitations.get()+"\n");
            if(invitations.isPresent()) {
                if (!invitations.get().isEmpty()) {
                    Iterator<ParticipantInvitation> iterator = invitations.get().iterator();
                    while (iterator.hasNext()) {
                        participantInvitationService.deleteInvitation(iterator.next(), researcher);
                    }
                }
            }
            delete(study.getId());
        }
        return true;
    }

    /**
     * create and associate a new Study.
     *
     * @param researcher to associate.
     * @param study the object.
     * @return the entity.
     */
    @Override
    public Optional<Study> addAndSaveStudy(Study study, Researcher researcher) {
        Optional<Set<ParticipantInstitution>> institutions= Optional.ofNullable(study.getInstitutions());
        study.setRecruiting(true);
        study.setState(State.PUBLISHED);
        study.setInstitutions(null);
        save(study);
        study.setResearcher(researcher);
        study.setStartDate(Instant.now());
        save(study);
        if(institutions.isPresent() && institutions.get()!=null) {
            if (institutions.get().size() != 0) {
                Iterator<ParticipantInstitution> iterator = institutions.get().iterator();
                while (iterator.hasNext()) {
                    participantInstitutionService.addAndSaveInstitution(study.getId(), iterator.next(), researcher);
                }
            }
        }
        Optional<Study> result = findOne(study.getId());
        log.debug("\n\nresultado estudio: "+study+"\n");
        return result;
    }

    /**
     * update Study.
     *
     * @param original to update.
     * @param study the object.
     * @return the entity updated.
     */
    @Override
    public Optional<Study> updateStudy(Study study,Study original) {
        if(original.getId().equals(study.getId())){
            original.setIcon(study.getIcon());
            original.setIconContentType(study.getIconContentType());
            if(!study.getName().equals(null) ){
                if(study.getName().length()!=0) {
                    original.setName(study.getName());
                }
            }
            original.setDescription(study.getDescription());
            original.setWebsite(study.getWebsite());
            original.setContactEmail(study.getContactEmail());
            original.setRequestedData(study.getRequestedData());
            original.setDataJustification(study.getDataJustification());
            if(study.getState()!=null){
                original.setState(study.getState());
                if(study.getState()==State.FINISHED){
                    original.setEndDate(Instant.now());
                }
            }
            if(study.isRecruiting()!=null ){
                original.setRecruiting(study.isRecruiting());
            }
            save(original);
            if(original.getInstitutions()!=null){
                Iterator<ParticipantInstitution> iterator=original.getInstitutions().iterator();
                original.setInstitutions(null);
                Iterator<ParticipantInstitution> iter=study.getInstitutions().iterator();
                while(iterator.hasNext() || iter.hasNext()){
                    if(iterator.hasNext()){
                        ParticipantInstitution object=iterator.next();
                        if(!study.getInstitutions().contains(object)) {
                            participantInstitutionService.deleteInstitution(object.getId(), original.getResearcher());
                        }
                    }
                    if(iter.hasNext()) {
                        participantInstitutionService.addAndSaveInstitution(original.getId(), iter.next(), original.getResearcher());

                    }
                }
            }
        }
        Optional<Study> result = findOne(original.getId());
        return result;
    }

    /**
     * check if the studycode is alredy used.
     *
     * @param studyCode to check if another study use this code.
     * @return true if the studyCode have already in use .
     */
    @Override
    public boolean alreadyCodeUsed(String studyCode) {
        Optional<Study> study=studyRepository.findOneByCodeOrderByStartDateAsc(studyCode);
        if(study.isPresent() && study.get()!=null){
            return  true;
        }else {
            return false;
        }
    }

    /**
     * create a studyDTO.
     *
     * @param study info need for create.
     * @return the study with reduced information.
     */
    @Override
    public StudyDTO getDTO(Study study) {
        int activeParticipants=participantRepository.findByStudyOrderByEntryDateAsc(study).get().size();
        StudyDTO studyDTO=new StudyDTO(study.getId(),study.getName(),study.getCode(),study.getIcon(),study.getStartDate(),
            study.getEndDate(),study.isRecruiting(),study.getContactEmail(),study.getWebsite(),study.getState(),
            study.getDescription(), study.getDataJustification(), study.getRequestedData(),activeParticipants);
        return  studyDTO;
    }

    /**
     * create a list of studyDTO.
     *
     * @param studies for get all studies.
     * @return list of entity.
     */
    @Override
    public ArrayList<StudyDTO> getStudiesDTO(Set<Study> studies) {
        ArrayList<StudyDTO> studyDTOS = new ArrayList<>();
        Iterator<Study> iterator = studies.iterator();
        while (iterator.hasNext()) {
            StudyDTO object = getDTO(iterator.next());
            studyDTOS.add(object);
        }
        return studyDTOS;
    }

    /**
     * get invitations of studies are accepted.
     *
     * @param study to get invitations.
     * @return list of the invitations are accepted .
     */
    @Override
    public Optional<Set<ParticipantInvitation>> getInvitationsAccepted(Study study) {
        return participantInvitationRepository.findByStudyAndStateOrderByIdAsc(study, true);
    }

    /**from study get the information of it and his participants with the mean of her health
     * data and the % of forms have been answered
     *
     * @param study for get info of it and his participants
     * @return the entity.
     */
    @Override
    public StudyManage createStudyManage(Study study) {
        StudyDTO studyDTO=getDTO(study);
        ArrayList<StudyParticipantManage> studyParticipantManages=new ArrayList<> ();
        Optional<Set<Participant>> participant= participantRepository.findByStudyOrderByEntryDateAsc(study);
        if(participant.isPresent()){
            Iterator<Participant> iterator=participant.get().iterator();
            while (iterator.hasNext()){
                Participant object=iterator.next();
                StudyParticipantManage studyParticipantManage = new StudyParticipantManage(object.getAnonymousId(),formAnswersService.percentage(study,object),dataService.getMeanDataLastWeek(object.getMobileUser(),study));
                studyParticipantManages.add(studyParticipantManage);
            }
        }
        StudyManage studyManage =new StudyManage(studyDTO,studyParticipantManages);
        return studyManage;
    }

    /**from study get the information of it and his forms with only title and id of it
     *
     * @param study for get info of it and his forms
     * @return the entity.
     */
    @Override
    public StudyManageMobile createStudyManageMobile(Study study,MobileUser mobileUser) {
        Optional<Set<Form>> forms=formRepository.findByStudyAndStateOrderByNameDesc(study, State.PUBLISHED);
        Optional<Participant> participant= participantRepository.findOneByStudyAndMobileUserOrderByEntryDateAsc(study, mobileUser);
        if(forms.isPresent() && participant.isPresent()) {
            ArrayList<FormsMobile> mobile = formService.getFormsMobile(forms.get(), participant.get());
            StudyManageMobile manageMobile = new StudyManageMobile(study.getName(), study.getCode(), study.getIcon(),
                study.getStartDate(), study.getEndDate(), study.isRecruiting(),study.getContactEmail(), study.getWebsite(),
                study.getState(), study.getDescription(), study.getDataJustification(),study.getRequestedData(), mobile);
            return manageMobile;
        }else{
            StudyManageMobile manageMobile = new StudyManageMobile(study.getName(), study.getCode(), study.getIcon(),
                study.getStartDate(), study.getEndDate(), study.isRecruiting(), study.getContactEmail(), study.getWebsite(),
                study.getState(), study.getDescription(), study.getDataJustification(), study.getRequestedData(), null);
            return manageMobile;
        }
    }
    /**
     * get one study
     *
     * @param code the code of study
     * @return the entity.
     */
    @Override
    public Optional<Study> getStudyByCode(String code) {
        return studyRepository.findOneByCodeOrderByStartDateAsc(code);
    }

    /**
     * get one study
     *
     * @param researcher to check if the studies belong to it
     * @return a list of entity.
     */
    @Override
    public Optional<Set<Study>> getStudyByResearcher(Researcher researcher) {
        return studyRepository.findByResearcherOrderByStartDateAsc(researcher);
    }

    /**
     * get one study
     *
     * @param researcher to check if the studies belong to it
     * @param state for check if the studies be in it state
     * @return a list of entity.
     */
    @Override
    public Optional<Set<Study>> getStudyByResearcherAndState(Researcher researcher, State state) {
        return studyRepository.findByResearcherAndStateOrderByStartDateAsc(researcher,state);
    }


    /**
     * return  number of studies are in this state.
     * @param state the state that i want check
     * @param researcher info need for check all studies.
     * @return the number.
     */
    @Override
    public int getStateStudies(Researcher researcher,State state) {
        return studyRepository.findByResearcherAndStateOrderByStartDateAsc(researcher,state).get().size();
    }
}

