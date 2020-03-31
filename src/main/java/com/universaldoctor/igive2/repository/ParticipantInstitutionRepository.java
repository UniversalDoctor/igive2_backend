package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.ParticipantInstitution;
import com.universaldoctor.igive2.domain.Study;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


/**
 * Spring Data MongoDB repository for the ParticipantInstitution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipantInstitutionRepository extends MongoRepository<ParticipantInstitution, String> {

    Optional<Set<ParticipantInstitution>> findByStudyOrderByIdDesc(Study study);
}
