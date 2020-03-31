package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.IGive2User;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.Study;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Researcher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResearcherRepository extends MongoRepository<Researcher, String> {

    Optional<Researcher> findOneByUserId(String userId);

}
