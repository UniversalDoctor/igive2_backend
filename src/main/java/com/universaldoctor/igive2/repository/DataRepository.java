package com.universaldoctor.igive2.repository;
import com.universaldoctor.igive2.domain.Data;
import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.enumeration.DataType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Spring Data MongoDB repository for the Data entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataRepository extends MongoRepository<Data, String> {

    Optional<Data> findTopByMobileUserAndDataOrderByDateDesc(MobileUser mobileUser, DataType dataType);

    Optional<Set<Data>> findByMobileUserOrderByDateDesc(MobileUser mobileUser);

    Optional<Set<Data>> findByMobileUserAndDataOrderByDateDesc(MobileUser mobileUser, DataType data);

    Optional<Data> findTopByMobileUserAndDataAndDateBetweenOrderByDateDesc(MobileUser mobileUser, DataType data, Instant dateGT, Instant dateLT);

    Optional<Set<Data>> findByMobileUserAndDataAndDateBetween(MobileUser mobileUser, DataType data, Instant dateGT, Instant dateLT);

}
