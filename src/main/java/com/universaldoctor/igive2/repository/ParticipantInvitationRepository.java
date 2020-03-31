package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.ParticipantInvitation;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.enumeration.State;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;


/**
 * Spring Data MongoDB repository for the ParticipantInvitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipantInvitationRepository extends MongoRepository<ParticipantInvitation, String> {

    Optional<Set<ParticipantInvitation>> findByStudyAndStateOrderByIdAsc(Study study, Boolean state);

    Optional<Set<ParticipantInvitation>> findByStudyOrderByIdAsc(Study study);

    Optional<ParticipantInvitation> findOneByEmailAndStudy(String email,Study study);

    Optional<ParticipantInvitation> findByEmail(String email);
}
