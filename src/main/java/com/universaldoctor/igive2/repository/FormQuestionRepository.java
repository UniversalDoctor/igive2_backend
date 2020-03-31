package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.Form;
import com.universaldoctor.igive2.domain.FormQuestion;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Spring Data MongoDB repository for the FormQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormQuestionRepository extends MongoRepository<FormQuestion, String> {

    Optional<Set<FormQuestion>> findByFormOrderByIdAsc(Form form);
}
