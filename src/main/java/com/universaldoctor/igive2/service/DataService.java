package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.Data;

import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.Participant;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.enumeration.DataType;
import com.universaldoctor.igive2.service.dto.DataBetween;
import com.universaldoctor.igive2.service.dto.DataDTO;
import com.universaldoctor.igive2.service.dto.DataMeanDTO;
import com.universaldoctor.igive2.service.dto.HypertensionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link Data}.
 */
public interface DataService {

    /**
     * Save a data.
     *
     * @param data the entity to save.
     * @return the persisted entity.
     */
    Data save(Data data);

    /**
     * Get all the data.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Data> findAll(Pageable pageable);


    /**
     * Get the "id" data.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Data> findOne(String id);

    /**
     * Delete the "id" data.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * put the "data" in mobile user profile.
     *
     * @param mobileUser the object on put data.
     * @param healthData the entity
     * @return the entity.
     */
    Optional<Data> putData(Data healthData,MobileUser mobileUser);

    /**
     * put the "data" in mobile user profile.
     *
     * @param mobileUser the object on put data.
     * @param hypertensionDTO the entity
     * @return the entity.
     */
    Set<Data> putHypertensionData(HypertensionDTO hypertensionDTO, MobileUser mobileUser);

    /**
     * put the "data" in participant.
     *
     * @param participant the id of the entity.
     * @param mobileUser to can get the data
     * @param data the type of data that you want put in participant.
     * @return participant object
     */
    ResponseEntity<Participant> putHealthDataInParticipant(DataType data, Participant participant, MobileUser mobileUser);

    /**
     * put one "data" in participant.
     *
     * @param participant the id of the entity.
     * @param data the object that you want put in participant.
     * @return data object
     */
    Optional<Data> putOneDataInParticipant(Data data, Participant participant);

    /*
    get all data that belongs at  mobile user
    * */
    Optional<Set<Data>> getData(MobileUser mobileUser);

    /*
    get all data of dataType order by date that belongs at  mobile user
    * */
    Optional<Set<Data>> getDataOfTypeDataOrderByDate(MobileUser mobileUser,DataType dataType);

    /*get the mean of last 7 days of each data type that belongs at one participant and it are in data requested of study*/
    ArrayList<DataMeanDTO> getMeanDataLastWeek(MobileUser mobileUser, Study study);

    /*get the mean of last 7 days of one data type that belongs at  participant*/
    DataMeanDTO getMeanDataLastWeekForTypeData(MobileUser mobileUser, DataType type);

    /*this method returns the last data that you are add in your profile
     * of all types of data*/
    ArrayList<Data> getLastDataOfAllHealthData(MobileUser mobileUser);

    /*this method returns the last data that you are add in your profile
     * of one type of data*/
    Optional<Data> getLastDataOfOneHealthData(MobileUser mobileUser,DataType dataType);

    /*this method returns the last data of data Type that belong at mobile user and were added between dategt and datelt*/
    Optional<Data> getTopByMobileUserAndDataAndDateBetweenOrderByDateDesc(MobileUser mobileUser, DataType dataType, Instant dateGT, Instant dateLT);

    /*this method returns the last data of data Type of each day between (dategt and datelt) that belong at mobile user */
    ResponseEntity<ArrayList<DataDTO>> getTopsByDataBetween(DataBetween dataBetween);

    /**
     * delete all the "data" in participant.
     *
     * @param mobileUser to check if the participant belongs it.
     * @param participant to get the data.
     * @return the entity.
     */
    ResponseEntity<Participant> deleteHealthDataInParticipant(Participant participant,MobileUser mobileUser);

    /**
     * Delete the entity data.
     *
     * @param dataType the type of data that we want delete.
     * @param mobileUser to delete of mobile user too.
     */
    void deleteDataForType(MobileUser mobileUser,DataType dataType);

    /**
     * Delete all objects of data that belongs at mobile user.
     *
     */
    void deleteAllData(MobileUser mobileUser);

}
