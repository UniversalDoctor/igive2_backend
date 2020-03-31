package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


/**
 * Spring Data MongoDB repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends MongoRepository<Answer, String> {

    Optional<Answer> findOneByFormQuestionAndFormAnswers(FormQuestion formQuestion,FormAnswers formAnswers);

    Optional<Set<Answer>> findByFormAnswersOrderByIdAsc(FormAnswers formAnswers);
}
