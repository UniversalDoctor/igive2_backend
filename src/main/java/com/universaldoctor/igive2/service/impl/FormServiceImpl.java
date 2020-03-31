package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.repository.FormQuestionRepository;
import com.universaldoctor.igive2.repository.ParticipantRepository;
import com.universaldoctor.igive2.repository.StudyRepository;
import com.universaldoctor.igive2.service.FormAnswersService;
import com.universaldoctor.igive2.service.FormQuestionService;
import com.universaldoctor.igive2.service.FormService;
import com.universaldoctor.igive2.repository.FormRepository;
import com.universaldoctor.igive2.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

/**
 * Service Implementation for managing {@link Form}.
 */
@Service
public class FormServiceImpl implements FormService {

    private final Logger log = LoggerFactory.getLogger(FormServiceImpl.class);

    private final FormRepository formRepository;

    private final StudyRepository studyRepository;
    private final FormQuestionRepository formQuestionRepository;
    private final ParticipantRepository participantRepository;
    private final FormQuestionService formQuestionService;
    private final FormAnswersService formAnswersService;

    public FormServiceImpl(FormRepository formRepository, StudyRepository studyRepository, FormQuestionRepository formQuestionRepository, ParticipantRepository participantRepository, FormQuestionService formQuestionService, FormAnswersService formAnswersService) {
        this.formRepository = formRepository;
        this.studyRepository = studyRepository;
        this.formQuestionRepository = formQuestionRepository;
        this.participantRepository = participantRepository;
        this.formQuestionService = formQuestionService;
        this.formAnswersService = formAnswersService;
    }

    /**
     * Save a form.
     *
     * @param form the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Form save(Form form) {
        log.debug("Request to save Form : {}", form);
        return formRepository.save(form);
    }

    /**
     * Get all the forms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Form> findAll(Pageable pageable) {
        log.debug("Request to get all Forms");
        return formRepository.findAll(pageable);
    }


    /**
     * Get one form by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Form> findOne(String id) {
        log.debug("Request to get Form : {}", id);
        return formRepository.findById(id);
    }

    /**
     * Delete the form by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Form : {}", id);
        formRepository.deleteById(id);
    }

    /**
     * Delete the questions of form by id.
     *
     * @param formId the id of the entity.
     * @return the optional entity.
     */
    @Override
    public  Optional<Form> deleteForm(String formId, Researcher researcher) {
        Optional<Form> form = findOne(formId);
        if (form.isPresent() && form.get()!=null) {
            Optional<Study> study = studyRepository.findById(form.get().getStudy().getId());
            if (study.isPresent()) {
                if (study.get().getResearcher().getId().equals(researcher.getId())) {
                    if (!form.get().getQuestions().isEmpty()) {
                        Iterator<FormQuestion> iterator = form.get().getQuestions().iterator();
                        while (iterator.hasNext()) {
                            formQuestionService.deleteFormQuestion(iterator.next().getId(),researcher);
                        }
                    }
                    Optional<Set<Participant>> participants= participantRepository.findByStudyOrderByEntryDateAsc(study.get());
                    if(participants.isPresent() && !participants.get().isEmpty()){
                        Iterator<Participant> iterator=participants.get().iterator();
                        while(iterator.hasNext()) {
                            formAnswersService.deleteFormAnswer(iterator.next(),form.get());
                        }
                    }
                    delete(form.get().getId());

                }
            }
        }
        return form;
    }
    /**
     * create and save one formQuestion by object formQuestion.
     *
     * @param study the id of form associate.
     * @param researcher to check that the form belong in it.
     * @param form the entity.
     * @return the entity.
     */
    @Override
    public Optional<Form> addAndSaveForm(Study study, Form form, Researcher researcher) {
        //Optional<Set<FormQuestion>> questions = Optional.ofNullable(form.getQuestions());
        if(study.getResearcher().getId().equals(researcher.getId())) {
            save(form);
            form.setStudy(study);
            save(form);
            /*if(questions.isPresent() && questions.get()!=null && !questions.get().isEmpty()){
                Iterator<FormQuestion> iterator= questions.get().iterator();
                while (iterator.hasNext()){
                    formQuestionService.addSaveNewFormQuestion(form,researcher,iterator.next());
                }
            }*/
        }
        Optional<Form> forms = findOne(form.getId());
        return forms;
    }
    /**
     * create a formsmobile.
     *
     * @param form the entity.
     * @param participant the entity.
     * @return the entity.
     */
    @Override
    public FormsMobile create(Form form, Participant participant) {
        FormsMobile formsMobile=new FormsMobile(form.getId(),form.getName(),
            formAnswersService.alreadyAnswered(participant,form));
        return formsMobile;

    }
    /**
     * Get  studiesDTO by studies.
     *
     * @param forms list original
     * @param participant to check if have responses.
     * @return the entity.
     */
    @Override
    public ArrayList<FormsMobile> getFormsMobile(Set<Form> forms, Participant participant) {
        ArrayList<FormsMobile> formsMobiles = new ArrayList<>();
        Iterator<Form> iterator = forms.iterator();
        while (iterator.hasNext()) {
            FormsMobile object = create(iterator.next(),participant);
            formsMobiles.add(object);
        }
        return formsMobiles;
    }


    @Override
    public Optional<Form> update(Form form, Form original) {
        if(!form.getName().equals(null)){
            if(form.getName().length()!=0){
                original.setName(form.getName());
            }
        }
        if(!form.getDescription().equals(null)){
            if(form.getDescription().length()!=0){
                original.setDescription(form.getDescription());
            }
        }
        if(!form.getState().equals(null)){
            original.setState(form.getState());
        }
        save(original);

        return findOne(original.getId());
    }

    /*from study we get all forms */
    @Override
    public Optional<Set<Form>> getForms(Study study) {
        return formRepository.findByStudyOrderByNameDesc(study);
    }

    /*return forms  with a specific state from study*/
    @Override
    public Optional<Set<Form>> getFormByStudyAndState(Study study, State state) {
        return formRepository.findByStudyAndStateOrderByNameDesc(study,state);
    }

    /*from study I catch the list of the forms, only if have it, and then with the list create another, but with only
     needed information (forms without the questions and with another interesting information to show)*/
    @Override
    public ArrayList<FormDTO> getFormsInfo(Study study) {
        ArrayList<FormDTO> result=new ArrayList<>();
        Optional<Set<Form>> forms=formRepository.findByStudyOrderByNameDesc(study);
        if(forms.isPresent() && forms.get()!=null){
            Iterator<Form> iterator=forms.get().iterator();
            while(iterator.hasNext()){
                Form form=iterator.next();
                if(form!=null){
                    result.add(getFormDTO(form));
                }
            }
        }
        return result;
    }

    /*create and return the object formDTo , it is need for the method getFormsInfo, because returns a form without
     questions and show another interesting information like number of answers or number of questions*/
    @Override
    public FormDTO getFormDTO(Form form) {
        int numberOfQuestions=0;
        int numberOfAnswers=0;
        Optional<Set<FormQuestion>> question=formQuestionRepository.findByFormOrderByIdAsc(form);
        Optional<Set<FormAnswers>> answers=formAnswersService.getFromFormsAndCompleted(form,true);
        if(question.isPresent() && question.get()!=null){
            numberOfQuestions=question.get().size();
        }
        if(answers.isPresent() && answers.get()!=null){
            numberOfAnswers=answers.get().size();
        }
        return new FormDTO(form.getId(),form.getName(),form.getDescription(),form.getState(),numberOfQuestions,numberOfAnswers);
    }

    /*from a form i get the info of it and a list of participant with her answers*/
    @Override
    public FormResult getFormResult(Form form) {
        ArrayList<ParticipantData> participantData=new ArrayList<>();
        FormResult result=new FormResult(form.getId(),form.getName(),form.getDescription(),participantData);
        Optional<Study> study=studyRepository.findById(form.getStudy().getId());
        Optional<Set<Participant>> participants=participantRepository.findByStudyOrderByEntryDateAsc(study.get());
        if(study.isPresent() && participants.isPresent()){
            Iterator<Participant> iterator=participants.get().iterator();
            while(iterator.hasNext()){
                Participant object=iterator.next();
                Optional<FormAnswers> formAnswers=formAnswersService.getFromParticipantAndForm(form,object);
                if(formAnswers.isPresent() && formAnswers.get().isCompleted()) {
                    Optional<Set<Answer>> answers = Optional.ofNullable(formAnswers.get().getResponses());
                    if (answers.isPresent()) {
                        ArrayList<QuestionsAnswers> questionsAnswers = new ArrayList<>();
                        Iterator<Answer> iter = answers.get().iterator();
                        while (iter.hasNext()) {
                            Answer answer = iter.next();
                            QuestionsAnswers questionsAnswers1 = new QuestionsAnswers(answer.getFormQuestion().getId(),
                                answer.getFormQuestion().getType(), answer.getFormQuestion().getQuestion(),answer.getFormQuestion().getOptions(),answer.getId(), answer.getData());
                            questionsAnswers.add(questionsAnswers1);
                        }
                        ParticipantData set=new ParticipantData(object.getAnonymousId(),questionsAnswers);
                        participantData.add(set);
                    }
                }
            }
        }
        return result;
    }

    /*create a new form from form with the same content but the new form will be associate to another study "study"*/
    @Override
    public Optional<Form> reportToAnotherStudy(Form form, Study study) {
     /*   Form object = new Form();
        object.setName(form.getName());
        object.setDescription(form.getDescription());
        object.setState(form.getState());
        object.setStudy(study);
        save(object);
        Optional<Set<FormQuestion>> questions = formQuestionRepository.findByForm(form);
        if (questions.isPresent() && questions.get()!=null && !questions.get().isEmpty()){
            Iterator<FormQuestion> iterator=questions.get().iterator();
            while(iterator.hasNext()){
                FormQuestion question=iterator.next();
                FormQuestion pregunta =new FormQuestion();
                pregunta.setForm(object);
                pregunta.setType(question.getType());
                pregunta.setQuestion(question.getQuestion());
                pregunta.setIsMandatory(question.isIsMandatory());
                formQuestionService.save(pregunta);
                object.addQuestions(pregunta);
                save(object);
            }
        }
        return findOne(object.getId());*/
     return  null;
    }

}
