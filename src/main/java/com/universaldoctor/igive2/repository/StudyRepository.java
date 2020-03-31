package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.enumeration.State;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Spring Data MongoDB repository for the Study entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudyRepository extends MongoRepository<Study, String> {

    Optional<Study> findOneByCodeOrderByStartDateAsc(String studyCode);

    Optional<Set<Study>> findByResearcherOrderByStartDateAsc(Researcher researcher);

    Optional<Set<Study>> findByResearcherAndStateOrderByStartDateAsc(Researcher researcher, State state);
}
