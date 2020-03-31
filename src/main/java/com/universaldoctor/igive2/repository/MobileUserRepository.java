package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.MobileUser;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data MongoDB repository for the MobileUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MobileUserRepository extends MongoRepository<MobileUser, String> {

    Optional<MobileUser> findOneByUserId(String userId);
}
