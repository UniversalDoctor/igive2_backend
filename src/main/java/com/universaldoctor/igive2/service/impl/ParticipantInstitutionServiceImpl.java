package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.repository.StudyRepository;
import com.universaldoctor.igive2.service.ParticipantInstitutionService;
import com.universaldoctor.igive2.domain.ParticipantInstitution;
import com.universaldoctor.igive2.repository.ParticipantInstitutionRepository;
import com.universaldoctor.igive2.service.StudyService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link ParticipantInstitution}.
 */
@Service
public class ParticipantInstitutionServiceImpl implements ParticipantInstitutionService {

    private final Logger log = LoggerFactory.getLogger(ParticipantInstitutionServiceImpl.class);

    private final ParticipantInstitutionRepository participantInstitutionRepository;

    private final StudyRepository studyRepository;

    public ParticipantInstitutionServiceImpl(ParticipantInstitutionRepository participantInstitutionRepository, StudyRepository studyRepository) {
        this.participantInstitutionRepository = participantInstitutionRepository;
        this.studyRepository = studyRepository;
    }

    /**
     * Save a participantInstitution.
     *
     * @param participantInstitution the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ParticipantInstitution save(ParticipantInstitution participantInstitution) {
        log.debug("Request to save ParticipantInstitution : {}", participantInstitution);
        return participantInstitutionRepository.save(participantInstitution);
    }

    /**
     * Get all the participantInstitutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<ParticipantInstitution> findAll(Pageable pageable) {
        log.debug("Request to get all ParticipantInstitutions");
        return participantInstitutionRepository.findAll(pageable);
    }


    /**
     * Get one participantInstitution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<ParticipantInstitution> findOne(String id) {
        log.debug("Request to get ParticipantInstitution : {}", id);
        return participantInstitutionRepository.findById(id);
    }

    /**
     * Delete the participantInstitution by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ParticipantInstitution : {}", id);
        participantInstitutionRepository.deleteById(id);
    }
    /**
     * Delete the questions of form by id.
     *
     * @param institutionId the id of the entity.
     * @param researcher to check that institution belongs it.
     * @return the optional entity.
     */
    @Override
    public void deleteInstitution(String institutionId, Researcher researcher) {
        Optional<ParticipantInstitution> participantInstitutions = findOne(institutionId);
        if(participantInstitutions.isPresent() && participantInstitutions.get()!=null){
            log.debug("\n\nentro en el metodo delete institution con objeto: "+participantInstitutions.get()+"\n");
            Optional<Study> study = studyRepository.findById(participantInstitutions.get().getStudy().getId());
            if (study.isPresent() && study.get().getResearcher().getId().equals(researcher.getId())) {
                study.get().removeInstitutions(participantInstitutions.get());
                delete(participantInstitutions.get().getId());
                studyRepository.save(study.get());
            }
        }
    }


    /**
     * create and save one formQuestion by objevt formQuestion.
     *
     * @param studyId the id of form associate.
     * @param researcher to check that the form belong in it.
     * @param institution the entity.
     * @return the entity.
     */
    @Override
    public void addAndSaveInstitution(String studyId, ParticipantInstitution institution, Researcher researcher) {
        log.debug("\n\nentro en el metodo addAndSaveIntitution con objeto: "+institution+"\n");
        Optional<Study> study = studyRepository.findById(studyId);
        if (study.isPresent() && study.get().getResearcher().getId().equals(researcher.getId())) {
            save(institution);
            institution.setStudy(study.get());
            save(institution);
            study.get().addInstitutions(institution);
            studyRepository.save(study.get());
        }
        log.debug("\n\ninstituciones que tiene estudio al salir del metodo add and save: "+study.get().getInstitutions()+"\n");
    }

    /*return all institutions of one study*/
    @Override
    public Optional<Set<ParticipantInstitution>> getInstitutions(Study study) {
        return participantInstitutionRepository.findByStudyOrderByIdDesc(study);
    }

    /* from study get all institutions that belong it, and then delete all*/
    @Override
    public void deleteAllIntitutionsOfStudy(Study study) {
        Optional<Set<ParticipantInstitution>> institutions=participantInstitutionRepository.findByStudyOrderByIdDesc(study);
        log.debug("\n\nelimino totas las instituciones  del estudio: "+study.getName()+" que son las siguientes: "+institutions.get()+"\n");
        if(institutions.isPresent() && institutions.get()!=null && !institutions.get().isEmpty()){
            Iterator<ParticipantInstitution> iterator=institutions.get().iterator();
            while(iterator.hasNext()){
                ParticipantInstitution object=iterator.next();
                if(object!=null){
                    deleteInstitution(object.getId(),study.getResearcher());
                }
            }
        }
    }
}
