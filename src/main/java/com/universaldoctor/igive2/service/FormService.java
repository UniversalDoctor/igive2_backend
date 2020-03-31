package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.Form;

import com.universaldoctor.igive2.domain.Participant;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.service.dto.FormDTO;
import com.universaldoctor.igive2.service.dto.FormResult;
import com.universaldoctor.igive2.service.dto.StudyManage;
import com.universaldoctor.igive2.service.dto.FormsMobile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link Form}.
 */
public interface FormService {

    /**
     * Save a form.
     *
     * @param form the entity to save.
     * @return the persisted entity.
     */
    Form save(Form form);

    /**
     * Get all the forms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Form> findAll(Pageable pageable);
    /**
     * Get all the FormDTO where FormAnswers is {@code null}.
     *
     * @return the list of entities.
     */
    //List<Form> findAllWhereFormAnswersIsNull();


    /**
     * Get the "id" form.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Form> findOne(String id);

    /**
     * Delete the "id" form.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Delete the "questions" form.
     *
     * @param formId the id of the entity.
     * @param researcher to check if the form belongs it .
     * @return
     */

    Optional<Form> deleteForm(String formId, Researcher researcher) ;

    /**
     * create and associate a new formQuestion.
     *
     * @param study to associate.
     * @param researcher to check if the form belongs.
     * @param form the object.
     * @return the entity.
     */
    Optional<Form> addAndSaveForm(Study study, Form form, Researcher researcher);

    /**
     * create a FormsMobile.
     *
     * @param form info need for create.
     * @param participant info need for create.
     * @return the persisted entity.
     */
    FormsMobile create(Form form, Participant participant);

    /**
     * create a list of formsMobile.
     * @param  participant info need for create.
     * @param  forms info need for create.
     * @return list of entity.
     */
    ArrayList<FormsMobile> getFormsMobile(Set<Form> forms, Participant participant);

    /* update the form "original" with the information of form "form"*/
    Optional<Form> update(Form form, Form original);

    /*from study we get all forms */
    Optional<Set<Form>> getForms(Study study);

    /*return forms  with a specific state from study*/
    Optional<Set<Form>> getFormByStudyAndState(Study study, State state);

    /*from study I catch the list of the forms, only if have it, and then with the list create another, but with only
     needed information (forms without the questions and with another interesting information to show)*/
    ArrayList<FormDTO> getFormsInfo(Study study);

    /*create and return the object formDTo , it is need for the method getFormsInfo, because returns a form without
     questions and show another interesting information like number of answers or number of questions*/
    FormDTO getFormDTO(Form form);

    /*from a form i get the info of it and a list of participant with her answers*/
    FormResult getFormResult(Form form);

    /*create a new form from form with the same content but the new form will be associate to another study "study"*/
    Optional<Form> reportToAnotherStudy(Form form, Study study);
}
