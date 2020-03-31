package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.Data;
import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.Participant;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.enumeration.DataType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Spring Data MongoDB repository for the Participant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipantRepository extends MongoRepository<Participant, String> {

    Optional<Set<Participant>> findByStudyOrderByEntryDateAsc(Study study);

    Optional<Participant> findOneByStudyAndMobileUserOrderByEntryDateAsc(Study study, MobileUser mobileUser);

    Optional<Set<Participant>> findByMobileUserOrderByEntryDateAsc(MobileUser mobileUser);


}
