package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.FormAnswers;
import com.universaldoctor.igive2.domain.FormQuestion;
import com.universaldoctor.igive2.repository.FormAnswersRepository;
import com.universaldoctor.igive2.service.AnswerService;
import com.universaldoctor.igive2.domain.Answer;
import com.universaldoctor.igive2.repository.AnswerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Answer}.
 */
@Service
public class AnswerServiceImpl implements AnswerService {

    private final Logger log = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final AnswerRepository answerRepository;

    private final FormAnswersRepository formAnswersRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, FormAnswersRepository formAnswersRepository) {
        this.answerRepository = answerRepository;
        this.formAnswersRepository = formAnswersRepository;
    }

    /**
     * Save a answer.
     *
     * @param answer the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Answer save(Answer answer) {
        log.debug("Request to save Answer : {}", answer);
        return answerRepository.save(answer);
    }

    /**
     * Get all the answers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Answer> findAll(Pageable pageable) {
        log.debug("Request to get all Answers");
        return answerRepository.findAll(pageable);
    }


    /**
     * Get one answer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Answer> findOne(String id) {
        log.debug("Request to get Answer : {}", id);
        return answerRepository.findById(id);
    }

    /**
     * Delete the answer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Answer : {}", id);
        answerRepository.deleteById(id);
    }

    /**
     * create one participant by mobile user and study.
     *
     * @param answer body .
     * @param formQuestion to associate.
     * @param formAnswers to associate.
     * @return the entity.
     */
    @Override
    public Answer create(String answer, FormQuestion formQuestion, FormAnswers formAnswers) {
        Answer a=new Answer();
        a.setData(answer);
        a.setFormAnswers(formAnswers);
        a.setFormQuestion(formQuestion);
        save(a);
        log.debug("Request to create new answer with response : {}", answer);
        return a;
    }

    /**
     * delete all answer of one form answer.
     *
     * @param formAnswers to get all answers .
     */
    @Override
    public void deleteAllAnswerOfForm(FormAnswers formAnswers) {
        Optional<Set<Answer>> answers= Optional.ofNullable(formAnswers.getResponses());
        log.debug("\n\nanswerws "+answers.get()+"\n");
        if(answers.isPresent()){
            Iterator<Answer> iterator=answers.get().iterator();
            while (iterator.hasNext()){
                Answer object=iterator.next();
                delete(object.getId());
            }
            formAnswers.getResponses().removeAll(formAnswers.getResponses());
            formAnswersRepository.save(formAnswers);
        }
    }

    /**
     * get answer.
     *
     * @param formAnswers to check if the answer belong it.
     * @param formQuestion to check if the answer belong it.
     * @return the entity.
     */
    @Override
    public Optional<Answer> getAnswer(FormAnswers formAnswers,FormQuestion formQuestion) {
        return answerRepository.findOneByFormQuestionAndFormAnswers(formQuestion,formAnswers);
    }


}
