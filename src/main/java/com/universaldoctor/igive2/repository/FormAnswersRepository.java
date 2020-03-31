package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.DataType;
import com.universaldoctor.igive2.domain.FormAnswers;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Spring Data MongoDB repository for the FormAnswers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormAnswersRepository extends MongoRepository<FormAnswers, String> {

    Optional<FormAnswers> findOneByParticipantAndForm(Participant participant, Form form);

    Optional<Set<FormAnswers>> findByParticipantOrderBySavedDateAsc(Participant participant);

    Optional<Set<FormAnswers>> findByFormAndCompletedOrderBySavedDateAsc(Form form,boolean completed);

    Optional<Set<FormAnswers>> findByParticipantAndCompletedOrderBySavedDateAsc(Participant participant, Boolean completed);

}
