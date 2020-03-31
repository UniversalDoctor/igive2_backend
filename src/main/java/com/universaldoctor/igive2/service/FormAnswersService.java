package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.*;

import com.universaldoctor.igive2.service.dto.AnswerMobile;
import com.universaldoctor.igive2.service.dto.FormAnswerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.mail.Part;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link FormAnswers}.
 */
public interface FormAnswersService {

    /**
     * Save a formAnswers.
     *
     * @param formAnswers the entity to save.
     * @return the persisted entity.
     */
    FormAnswers save(FormAnswers formAnswers);

    /**
     * Get all the formAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormAnswers> findAll(Pageable pageable);


    /**
     * Get the "id" formAnswers.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormAnswers> findOne(String id);

    /**
     * Delete the "id" formAnswers.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * create formAnswers.
     *
     * @param form to associate.
     * @param participant to associate.
     * @return the entity.
     */
    FormAnswers create(Form form, Participant participant);

    /**
     * create and add answers in a formanswer.
     *
     * @param answers the id of the entity.
     * @param form to associate.
     * @param participant to associate.
     * @return the entity.
     */
    ResponseEntity<FormAnswers> saveResponses(ArrayList<AnswerMobile> answers, Form form, Participant participant);

    /**
     * Delete all formAnswers.
     *
     * @param participant the id of the entity.
     */
    void deleteAllFormAnswer(Participant participant);

    /**
     * Delete  formAnswers.
     *
     * @param form
     * @param participant the id of the entity.
     */
    void deleteFormAnswer(Participant participant, Form form);

    /**
     * check if the form already answered
     *
     * @param form to associate.
     * @param participant to associate.
     * @return the entity.
     */
    boolean alreadyAnswered(Participant participant,Form form);

    /**
     * check if the form already answered
     *
     * @param study to associate.
     * @return the entity.
     */
    int percentage(Study study,Participant participant);

    /**
     * get the formanswerDTO
     *
     * @param form to check.
     * @param participant to chek.
     * @return the entity.
     */
    FormAnswerDTO getAnswers(Form form, Participant participant);

    /**
     * get the formanswer
     *
     * @param form to check.
     * @param participant to chek.
     * @return the entity.
     */
    Optional<FormAnswers> getFromParticipantAndForm(Form form, Participant participant);

    /**
     * get All  formanswers that belong to participant
     *
     * @param participant to chek.
     * @return list of the entity.
     */
    Optional<Set<FormAnswers>> getFromParticipan(Participant participant);

    /**
     * get a list of formanswer
     *
     * @param form to check.
     * @param completed to chek.
     * @return the list of entity.
     */
    Optional<Set<FormAnswers>> getFromFormsAndCompleted(Form form, boolean completed);
}
