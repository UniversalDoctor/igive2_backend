package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.Form;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.enumeration.State;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


/**
 * Spring Data MongoDB repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormRepository extends MongoRepository<Form, String> {
    Optional<Set<Form>> findByStudyOrderByNameDesc(Study study);

    Optional<Set<Form>>findByStudyAndStateOrderByNameDesc(Study study, State state);
}
