package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.service.AnswerService;
import com.universaldoctor.igive2.service.FormAnswersService;
import com.universaldoctor.igive2.service.dto.AnswerMobile;
import com.universaldoctor.igive2.service.dto.FormAnswerDTO;
import com.universaldoctor.igive2.service.dto.QuestionsAnswers;
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
 * Service Implementation for managing {@link FormAnswers}.
 */
@Service
public class FormAnswersServiceImpl implements FormAnswersService {

    private final Logger log = LoggerFactory.getLogger(FormAnswersServiceImpl.class);

    private final FormAnswersRepository formAnswersRepository;

    private  final FormQuestionRepository formQuestionRepository;
    private final FormRepository formRepository;
    private final ParticipantRepository participantRepository;
    private final AnswerService answerService;
    private final AnswerRepository answerRepository;


    public FormAnswersServiceImpl(FormAnswersRepository formAnswersRepository, FormQuestionRepository formQuestionRepository,
                                  FormRepository formRepository, ParticipantRepository participantRepository, AnswerService answerService, AnswerRepository answerRepository) {
        this.formAnswersRepository = formAnswersRepository;
        this.formQuestionRepository=formQuestionRepository;
        this.formRepository = formRepository;
        this.participantRepository = participantRepository;
        this.answerService = answerService;
        this.answerRepository = answerRepository;
    }

    /**
     * Save a formAnswers.
     *
     * @param formAnswers the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FormAnswers save(FormAnswers formAnswers) {
        log.debug("Request to save FormAnswers : {}", formAnswers);
        return formAnswersRepository.save(formAnswers);
    }

    /**
     * Get all the formAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<FormAnswers> findAll(Pageable pageable) {
        log.debug("Request to get all FormAnswers");
        return formAnswersRepository.findAll(pageable);
    }


    /**
     * Get one formAnswers by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<FormAnswers> findOne(String id) {
        log.debug("Request to get FormAnswers : {}", id);
        return formAnswersRepository.findById(id);
    }

    /**
     * Delete the formAnswers by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete FormAnswers : {}", id);
        formAnswersRepository.deleteById(id);
    }

    /**
     * create a new form answer.
     *
     * @param form to associate.
     * @param participant to associate.
     * @return boolean.
     */
    @Override
    public FormAnswers create(Form form, Participant participant) {
        log.debug("Request to create new  FormAnswers : {}");
        FormAnswers formAnswer=new FormAnswers();
        formAnswer.setCompleted(false);
        formAnswer.setSavedDate(Instant.now());
        formAnswer.setForm(form);
        formAnswer.setParticipant(participant);
        save(formAnswer);
        return formAnswer;
    }

    @Override
    public ResponseEntity<FormAnswers> saveResponses(ArrayList<AnswerMobile> answers, Form form, Participant participant) {
        Optional<FormAnswers> formAnswers=formAnswersRepository.findOneByParticipantAndForm(participant,form);
        if(formAnswers.isPresent()){
            Iterator<AnswerMobile> iter = answers.iterator();
            while (iter.hasNext()){
                AnswerMobile answerMobile= iter.next();
                Optional<FormQuestion> formQuestion = formQuestionRepository.findById(answerMobile.getQuestionId());
                if(formQuestion.isPresent()){
                    if (!formQuestion.get().getForm().getId().equals(form.getId())) {
                        ResponseEntity error = new ResponseEntity("the questions not correspond with the questions of form", HttpStatus.BAD_REQUEST);
                        return error;

                    }
                    Optional<Answer> answer=answerService.getAnswer(formAnswers.get(),formQuestion.get());
                    if(answer.isPresent()){
                        answer.get().setData(answerMobile.getResponse());
                        answerRepository.save(answer.get());
                    }else {
                        addNewResponse(formAnswers.get(), answerMobile, formQuestion.get(), participant);
                    }
                }
            }
        }else {
            Iterator<AnswerMobile> iterator = answers.iterator();
            FormAnswers formAnswer = create(form, participant);
            while (iterator.hasNext()) {
                AnswerMobile object = iterator.next();
                Optional<FormQuestion> formQuestion = formQuestionRepository.findById(object.getQuestionId());
                if (formQuestion.isPresent()) {
                    if (!formQuestion.get().getForm().getId().equals(form.getId())) {
                        ResponseEntity error = new ResponseEntity("the questions not correspond with the questions of form", HttpStatus.BAD_REQUEST);
                        return error;

                    }
                    addNewResponse(formAnswer, object, formQuestion.get(), participant);
                }
            }
            formAnswer.setCompleted(alreadyAnswered(participant,form));
            save(formAnswer);
            Optional<FormAnswers> optionalAnswers = Optional.ofNullable(formAnswer);
            return ResponseUtil.wrapOrNotFound(optionalAnswers);
        }
        Optional<FormAnswers> optionalAnswers =formAnswersRepository.findOneByParticipantAndForm(participant,form);
        optionalAnswers.get().setCompleted(alreadyAnswered(participant,form));
        save(formAnswers.get());
        return ResponseUtil.wrapOrNotFound(optionalAnswers);
    }

    /*create and add a new response to one forms answer*/
    public void addNewResponse(FormAnswers formAnswer,AnswerMobile answerMobile,FormQuestion formQuestion,Participant participant){
        String response = answerMobile.getResponse();
        Answer answer = answerService.create(response, formQuestion, formAnswer);
        formAnswer.addResponses(answer);
        save(formAnswer);
    }

    /**
     * check if the form already answered
     *
     * @param form to associate.
     * @param participant to associate.
     * @return the entity.
     */
    @Override
    public boolean alreadyAnswered(Participant participant, Form form) {
        Optional<FormAnswers> formAnswers=formAnswersRepository.findOneByParticipantAndForm(participant,form);
        Optional<Set<FormQuestion>> question=formQuestionRepository.findByFormOrderByIdAsc(form);
        if(formAnswers.isPresent() && question.isPresent() && question!=null){
            if(question.get().size()==formAnswers.get().getResponses().size()){
                log.debug("\nalreadyAnswered: "+true+"\n");
                return true;
            }
        }
        log.debug("\nalreadyAnswered: "+false+"\n");
        return false;
    }

    /*este metodo devuelve el porcentaje de los formularios activos de un estudio que ha reespondido un participante, si el numero
    * de formularios activos es el mismo que el numero de conjunto de respuestas completadas del participante el porcentaje seria 100%*/
    @Override
    public int percentage(Study study, Participant participant) {
        Optional<Set<Form>> forms=formRepository.findByStudyAndStateOrderByNameDesc(study, State.PUBLISHED);
        Optional<Set<FormAnswers>> answers=formAnswersRepository.findByParticipantAndCompletedOrderBySavedDateAsc(participant,true);
        if(forms.isPresent() && answers.isPresent()){
            if(forms.get().size()!=0) {
                return (answers.get().size() / forms.get().size() * 100);
            }
        }
        return 0;
    }

    /**
     * get de formanswerDTO
     *
     * @param form to check.
     * @param participant to chek.
     * @return the entity.
     */
    @Override
    public FormAnswerDTO getAnswers(Form form, Participant participant) {
        log.debug("\n\n metodo getanswers\n");
        FormAnswerDTO result=new FormAnswerDTO(form.getId(),form.getName(),form.getDescription(),null);
        Optional<FormAnswers> formAnswers=formAnswersRepository.findOneByParticipantAndForm(participant,form);
        if(formAnswers.isPresent()) {
            log.debug("\n\n hay un form answer presente\n");
            Optional<Set<Answer>> answers = Optional.ofNullable(formAnswers.get().getResponses());
            if (answers.isPresent()) {
                ArrayList<QuestionsAnswers> questionsAnswers = new ArrayList<>();
                Iterator<Answer> iterator = answers.get().iterator();
                while (iterator.hasNext()) {
                    Answer object = iterator.next();
                    QuestionsAnswers questionsAnswers1 = new QuestionsAnswers(object.getFormQuestion().getId(),object.getFormQuestion().getType(), object.getFormQuestion().getQuestion(),
                        object.getFormQuestion().getOptions(), object.getId(), object.getData());
                    questionsAnswers.add(questionsAnswers1);
                }
                result.setResponses(questionsAnswers);
                return result;
            }
        }
        log.debug("\n\n no hay un form answer presente\n");
        Iterator<FormQuestion> iterator= form.getQuestions().iterator();
        ArrayList<QuestionsAnswers> questionsAnswers = new ArrayList<>();
        while(iterator.hasNext()){
            FormQuestion formQuestion=iterator.next();
            QuestionsAnswers pregunta = new QuestionsAnswers(formQuestion.getId(), formQuestion.getType(),  formQuestion.getQuestion(), formQuestion.getOptions(),null,null);
            questionsAnswers.add(pregunta);
        }
        result.setResponses(questionsAnswers);
        return result;

    }

    /**
     * get de formanswer
     *
     * @param form to check.
     * @param participant to chek.
     * @return the entity.
     */
    @Override
    public Optional<FormAnswers> getFromParticipantAndForm(Form form, Participant participant) {
        return formAnswersRepository.findOneByParticipantAndForm(participant,form);
    }

    /**
     * get All  formanswers that belong to participant
     *
     * @param participant to chek.
     * @return list of the entity.
     */
    @Override
    public Optional<Set<FormAnswers>> getFromParticipan(Participant participant) {
        return formAnswersRepository.findByParticipantOrderBySavedDateAsc(participant);
    }

    /**
    * get a list of formanswer
     *
     * @param form to check.
     * @param completed to chek.
     * @return the list of entity.
     * */
    @Override
    public Optional<Set<FormAnswers>> getFromFormsAndCompleted(Form form, boolean completed) {
        return formAnswersRepository.findByFormAndCompletedOrderBySavedDateAsc(form,completed);
    }

    /**
     * Delete all formAnswers.
     *
     * @param participant the id of the entity.
     */
    @Override
    public void deleteAllFormAnswer(Participant participant) {
        Optional<Set<FormAnswers>> formAnswers= formAnswersRepository.findByParticipantOrderBySavedDateAsc(participant);
        if(formAnswers.isPresent()){
            Iterator<FormAnswers> iterator=formAnswers.get().iterator();
            while(iterator.hasNext()){
                FormAnswers object=iterator.next();
                answerService.deleteAllAnswerOfForm(object);
                delete(object.getId());
            }
        }
    }

    /**
     * Delete  formAnswers.
     *
     * @param form
     * @param participant the id of the entity.
     */
    @Override
    public void deleteFormAnswer(Participant participant, Form form) {
        Optional<FormAnswers> formAnswers= formAnswersRepository.findOneByParticipantAndForm(participant,form);
        if(formAnswers.isPresent()){
            answerService.deleteAllAnswerOfForm(formAnswers.get());
            delete(formAnswers.get().getId());
        }
    }


}
