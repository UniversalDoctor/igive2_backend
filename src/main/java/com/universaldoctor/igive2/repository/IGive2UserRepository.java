package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.IGive2User;
import com.universaldoctor.igive2.domain.MobileUser;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data MongoDB repository for the IGive2User entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IGive2UserRepository extends MongoRepository<IGive2User, String> {
}
