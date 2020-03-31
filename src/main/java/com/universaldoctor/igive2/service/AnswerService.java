package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.Answer;

import com.universaldoctor.igive2.domain.FormAnswers;
import com.universaldoctor.igive2.domain.FormQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link Answer}.
 */
public interface AnswerService {

    /**
     * Save a answer.
     *
     * @param answer the entity to save.
     * @return the persisted entity.
     */
    Answer save(Answer answer);

    /**
     * Get all the answers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Answer> findAll(Pageable pageable);


    /**
     * Get the "id" answer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Answer> findOne(String id);

    /**
     * Delete the "id" answer.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * create answer.
     *
     * @param answer the body of answer.
     * @param formAnswers to associate.
     * @param formQuestion to associate.
     * @return the entity.
     */
    Answer create(String answer, FormQuestion formQuestion, FormAnswers formAnswers);

    /**
     * delete all answer of one form answer.
     *
     * @param formAnswers to get all answers .
     */
    void deleteAllAnswerOfForm(FormAnswers formAnswers);

    /**
     * get answer.
     *
     * @param formAnswers to check if the answer belong it.
     * @param formQuestion to check if the answer belong it.
     * @return the entity.
     */
    Optional<Answer> getAnswer(FormAnswers formAnswers,FormQuestion formQuestion);
}
