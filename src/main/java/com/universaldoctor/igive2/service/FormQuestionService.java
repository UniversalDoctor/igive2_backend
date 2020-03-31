package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.Form;
import com.universaldoctor.igive2.domain.FormQuestion;

import com.universaldoctor.igive2.domain.Researcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link FormQuestion}.
 */
public interface FormQuestionService {

    /**
     * Save a formQuestion.
     *
     * @param formQuestion the entity to save.
     * @return the persisted entity.
     */
    FormQuestion save(FormQuestion formQuestion);

    /**
     * Get all the formQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormQuestion> findAll(Pageable pageable);
    /**
     * Get all the FormQuestionDTO where Answer is {@code null}.
     *
     * @return the list of entities.
     */
    List<FormQuestion> findAllWhereAnswerIsNull();


    /**
     * Get the "id" formQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormQuestion> findOne(String id);

    /**
     * Delete the "id" formQuestion.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Delete the "id" formQuestion.
     *
     * @param id the id of the entity.
     * @param researcher to check if belongs.
     * @return the entity.
     */
     Optional<FormQuestion> deleteFormQuestion(String id, Researcher researcher) ;

    /**
     * create and associate a new formQuestion.
     *
     * @param form to associate.
     * @param researcher to check if the form belongs.
     * @param formQuestion the object.
     * @return the entity.
     */
     Optional<FormQuestion> addSaveNewFormQuestion(Form form, Researcher researcher, FormQuestion formQuestion);

    /**
     * update  one formQuestion.
     *
     * @param original the entity that i want edit.
     * @param formQuestion the object.
     * @return the entity.
     */
     Optional<FormQuestion> update(FormQuestion formQuestion, FormQuestion original);

    /**
     * get  the list of entity
     *
     * @param form to check the questions that belong it
     * @return the list of entity.
     */
    Optional<Set<FormQuestion>> getFromFom(Form form);

}
