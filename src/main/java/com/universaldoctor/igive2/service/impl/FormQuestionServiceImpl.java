package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.QuestionType;
import com.universaldoctor.igive2.repository.FormRepository;
import com.universaldoctor.igive2.repository.StudyRepository;
import com.universaldoctor.igive2.service.AnswerService;
import com.universaldoctor.igive2.service.FormQuestionService;
import com.universaldoctor.igive2.domain.FormQuestion;
import com.universaldoctor.igive2.repository.FormQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link FormQuestion}.
 */
@Service
public class FormQuestionServiceImpl implements FormQuestionService  {

    private final Logger log = LoggerFactory.getLogger(FormQuestionServiceImpl.class);

    private final FormQuestionRepository formQuestionRepository;

    private final FormRepository formRepository;
    private final StudyRepository studyRepository;

    public FormQuestionServiceImpl(FormQuestionRepository formQuestionRepository, FormRepository formRepository, StudyRepository studyRepository) {
        this.formQuestionRepository = formQuestionRepository;
        this.formRepository = formRepository;
        this.studyRepository = studyRepository;
    }

    /**
     * Save a formQuestion.
     *
     * @param formQuestion the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FormQuestion save(FormQuestion formQuestion) {
        log.debug("Request to save FormQuestion : {}", formQuestion);
        return formQuestionRepository.save(formQuestion);
    }

    /**
     * Get all the formQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<FormQuestion> findAll(Pageable pageable) {
        log.debug("Request to get all FormQuestions");
        return formQuestionRepository.findAll(pageable);
    }



    /**
    *  Get all the formQuestions where Answer is {@code null}.
     *  @return the list of entities.
     */
    public List<FormQuestion> findAllWhereAnswerIsNull() {
        log.debug("Request to get all formQuestions where Answer is null");
        return StreamSupport
            .stream(formQuestionRepository.findAll().spliterator(), false)
            .filter(formQuestion -> formQuestion.getAnswer() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one formQuestion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<FormQuestion> findOne(String id) {
        log.debug("Request to get FormQuestion : {}", id);
        return formQuestionRepository.findById(id);
    }

    /**
     * Delete the formQuestion by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete FormQuestion : {}", id);
        formQuestionRepository.deleteById(id);
    }

    /**
     * delete one formQuestion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<FormQuestion> deleteFormQuestion(String id, Researcher researcher) {
        Optional<FormQuestion> formQuestion =findOne(id);
        if(formQuestion.isPresent()) {
            Optional<Form> form = formRepository.findById(formQuestion.get().getForm().getId());
            if (form.isPresent() ) {
                Optional<Study> study = studyRepository.findById(form.get().getStudy().getId());
                if (study.isPresent()) {
                    if (researcher.getId().equals(study.get().getResearcher().getId())) {
                        form.get().removeQuestions(formQuestion.get());
                        formRepository.save(form.get());
                        delete(formQuestion.get().getId());
                    }
                }
            }
        }
        return  formQuestion;
    }

    /**
     * create and save one formQuestion by object formQuestion.
     *
     * @param form the id of form associate.
     * @param researcher to check that the form belong in it.
     * @param formQuestion the entity.
     * @return the entity.
     */
    @Override
    public Optional<FormQuestion> addSaveNewFormQuestion(Form form, Researcher researcher, FormQuestion formQuestion) {
        Optional<Study> study=studyRepository.findById(form.getStudy().getId());
        if(study.isPresent()){
            if(researcher.getId().equals(study.get().getResearcher().getId())) {
                save(formQuestion);
                formQuestion.setForm(form);
                save(formQuestion);
                form.addQuestions(formQuestion);
                formRepository.save(form);
            }
        }
        Optional<FormQuestion> forms = findOne(formQuestion.getId());
        return forms;
    }

    /**
     * Update one  formQuestion.
     *
     * @param original the entity that i want edit.
     * @param formQuestion the object.
     * @return the entity.
     */
    @Override
    public Optional<FormQuestion> update(FormQuestion formQuestion, FormQuestion original) {
        if(!formQuestion.getQuestion().equals(null)){
            if(formQuestion.getQuestion().length()!=0){
                original.setQuestion(formQuestion.getQuestion());
            }
        }
        if(!formQuestion.isIsMandatory().equals(null)){
            original.setIsMandatory(formQuestion.isIsMandatory());
        }
        if(!formQuestion.getType().equals(null)){
            original.setType(formQuestion.getType());
        }
        original.setOptions(formQuestion.getOptions());
        save(original);
        return findOne(original.getId());
    }

    /**
     * get  the list of entity
     *
     * @param form to check the questions that belong it
     * @return the list of entity.
     */
    @Override
    public Optional<Set<FormQuestion>> getFromFom(Form form) {
        return formQuestionRepository.findByFormOrderByIdAsc(form);
    }


}
