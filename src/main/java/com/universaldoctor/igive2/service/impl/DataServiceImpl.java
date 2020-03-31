package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.Participant;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.enumeration.DataType;
import com.universaldoctor.igive2.repository.MobileUserRepository;
import com.universaldoctor.igive2.repository.ParticipantRepository;
import com.universaldoctor.igive2.repository.StudyRepository;
import com.universaldoctor.igive2.service.DataService;
import com.universaldoctor.igive2.domain.Data;
import com.universaldoctor.igive2.repository.DataRepository;
import com.universaldoctor.igive2.service.dto.DataBetween;
import com.universaldoctor.igive2.service.dto.DataDTO;
import com.universaldoctor.igive2.service.dto.DataMeanDTO;
import com.universaldoctor.igive2.service.dto.HypertensionDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Service Implementation for managing {@link Data}.
 */
@Service
public class DataServiceImpl implements DataService {

    private final Logger log = LoggerFactory.getLogger(DataServiceImpl.class);

    private final DataRepository dataRepository;

    private final StudyRepository studyRepository;
    private final ParticipantRepository participantRepository;

    public DataServiceImpl(DataRepository dataRepository, StudyRepository studyRepository, ParticipantRepository participantRepository) {
        this.dataRepository = dataRepository;
        this.studyRepository = studyRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * Save a data.
     *
     * @param data the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Data save(Data data) {
        log.debug("Request to save Data : {}", data);
        return dataRepository.save(data);
    }

    /**
     * Get all the data.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Data> findAll(Pageable pageable) {
        log.debug("Request to get all Data");
        return dataRepository.findAll(pageable);
    }


    /**
     * Get one data by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Data> findOne(String id) {
        log.debug("Request to get Data : {}", id);
        return dataRepository.findById(id);
    }

    /**
     * Delete the data by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Data : {}", id);
        dataRepository.deleteById(id);
    }

    /**
     * put the "data" in participant.
     *
     * @param participant the id of the entity.
     * @param mobileUser to can get the data
     * @param data the type of data that you want put in participant.
     * @return participant object
     */
    @Override
    public ResponseEntity<Participant> putHealthDataInParticipant(DataType data, Participant participant, MobileUser mobileUser) {
        Optional<Study> study=studyRepository.findById(participant.getStudy().getId());
        if (study.isPresent()) {
            log.debug("\n\nstudy: "+study.get()+"\n");
            if (participant.getMobileUser().getId().equals(mobileUser.getId())) {
                String dataRequested=study.get().getRequestedData();
                if(dataRequested.contains("BLOODPRESSURE")){
                    dataRequested=dataRequested.replace("BLOODPRESSURE","SYSTOLIC, DYASTOLIC");
                }
                if ( dataRequested.contains(data.name())) {
                    Optional<Set<Data>> healthData =dataRepository.findByMobileUserAndDataOrderByDateDesc(mobileUser,data);
                    if(healthData.isPresent() && healthData.get()!=null) {
                        participant.getParticipantData().addAll(healthData.get());
                        participantRepository.save(participant);
                        Optional<Participant> optionalParticipant = participantRepository.findById(participant.getId());
                        return ResponseUtil.wrapOrNotFound(optionalParticipant);
                    }
                    ResponseEntity error=new ResponseEntity("The user does not have any data of this type",HttpStatus.NOT_FOUND);
                    return error;
                }
                ResponseEntity error=new ResponseEntity("the data does not correspond with any type of data required of study",HttpStatus.CONFLICT);
                return error;
            }
            ResponseEntity error=new ResponseEntity("the participant not correspond with this profile or the gate are wrong",HttpStatus.CONFLICT);
            return error;
        }
        ResponseEntity error=new ResponseEntity("the study is not present in participant",HttpStatus.CONFLICT);
        return error;
    }

    /**
     * put one "data" in participant.
     *
     * @param participant the id of the entity.
     * @param data the object that you want put in participant.
     * @return data object
     */
    @Override
    public Optional<Data> putOneDataInParticipant(Data data, Participant participant) {
        Optional<Study> study=studyRepository.findById(participant.getStudy().getId());
        if(study.isPresent()) {
            String dataRequested = study.get().getRequestedData();
            if (dataRequested.contains("BLOODPRESSURE")) {
                dataRequested = dataRequested.replace("BLOODPRESSURE", "SYSTOLIC, DYASTOLIC");
            }
            if (dataRequested.contains(data.getData().name())){
                participant.getParticipantData().add(data);
                participantRepository.save(participant);
                return dataRepository.findById(data.getId());
            }
        }
        return Optional.empty();
    }

    /**
     * delete all the "data" in participant.
     *
     * @param mobileUser to check if the participant belongs it.
     * @param participant to get the data.
     * @return the entity.
     */
    @Override
    public ResponseEntity<Participant> deleteHealthDataInParticipant(Participant participant,MobileUser mobileUser) {
        if (participant.getMobileUser().getId().equals(mobileUser.getId())) {
            Optional<Set<Data>> data = Optional.ofNullable(participant.getParticipantData());
            if (data.isPresent()) {
                participant.getParticipantData().removeAll(participant.getParticipantData());
                participantRepository.save(participant);
            }
            Optional<Participant> optionalParticipant=participantRepository.findById(participant.getId());
            return ResponseUtil.wrapOrNotFound(optionalParticipant);
        }
        ResponseEntity error=new ResponseEntity("the id does not correspond to any participant of this profile or the gate are wrong",HttpStatus.CONFLICT);
        return error;
    }



    /**
     * Delete all objects of data that belongs at mobile user.
     *
     */
    @Override
    public void deleteAllData(MobileUser mobileUser) {
        Optional<Set<Data>> data= dataRepository.findByMobileUserOrderByDateDesc(mobileUser);
        if(data.isPresent()){
            Iterator<Data> iterator=data.get().iterator();
            while (iterator.hasNext()){
                delete(iterator.next().getId());
            }
        }
    }

    /**
     * put the "data" in mobile user profile.
     *
     * @param mobileUser the object on put data.
     * @param healthData the entity
     * @return the entity.
     */
    @Override
    public Optional<Data> putData(Data healthData, MobileUser mobileUser) {
        healthData.setDate(Instant.now());
        healthData.setMobileUser(mobileUser);
        save(healthData);
        Optional<Data> data=findOne(healthData.getId());
        return data;
    }

    /**
     * put the "data" in mobile user profile.
     *
     * @param mobileUser the object on put data.
     * @param hypertensionDTO the entity
     * @return the entity.
     */
    @Override
    public Set<Data> putHypertensionData(HypertensionDTO hypertensionDTO, MobileUser mobileUser) {
        Data sistolic=new Data();
        sistolic.setDate(Instant.now());
        sistolic.setData(DataType.SYSTOLIC);
        sistolic.setValue(hypertensionDTO.getSystolic());
        sistolic.setMobileUser(mobileUser);
        save(sistolic);
        Data dyastolic=new Data();
        dyastolic.setDate(Instant.now());
        dyastolic.setData(DataType.DYASTOLIC);
        dyastolic.setValue(hypertensionDTO.getDyastolic());
        dyastolic.setMobileUser(mobileUser);
        save(dyastolic);
        Set<Data> res = new HashSet<>();
        res.add(sistolic);
        res.add(dyastolic);
        return res;
    }

    /*
        get all data that belongs at  mobile user
        * */
    @Override
    public Optional<Set<Data>> getData(MobileUser mobileUser) {
        return dataRepository.findByMobileUserOrderByDateDesc(mobileUser);
    }

    /*
    get all data of dataType that belongs at  mobile user
    * */
    @Override
    public Optional<Set<Data>> getDataOfTypeDataOrderByDate(MobileUser mobileUser,DataType dataType) {
        return dataRepository.findByMobileUserAndDataOrderByDateDesc(mobileUser,dataType);
    }

    /*get the mean of last 7 days of each data type that belongs at  mobile user and it are in data requested of study*/
    @Override
    public ArrayList<DataMeanDTO> getMeanDataLastWeek(MobileUser mobileUser, Study study) {
        ArrayList<DataMeanDTO> result=new ArrayList<>();
        String dataRequired=study.getRequestedData();
        Optional<Participant> participant=participantRepository.findOneByStudyAndMobileUserOrderByEntryDateAsc(study,mobileUser);
        if(participant.isPresent() && participant.get()!=null) {
            if (dataRequired.contains("WEIGHT")) {
                Optional<DataMeanDTO> data = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.WEIGHT));
                if (data.isPresent()) {
                    result.add(data.get());
                }
            }
            if (dataRequired.contains("HEIGHT")) {
                Optional<DataMeanDTO> data = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.HEIGHT));
                if (data.isPresent()) {
                    result.add(data.get());
                }
            }
            if (dataRequired.contains("BLOODPRESSURE")) {
                Optional<DataMeanDTO> datas = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.SYSTOLIC));
                Optional<DataMeanDTO> datad = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.DYASTOLIC));
                if (datas.isPresent() && datad.isPresent()) {
                    DataMeanDTO d = new DataMeanDTO("BLOODPRESSURE", datas.get().getMeanValue() + "/" + datad.get().getMeanValue());
                    result.add(d);
                }
            }
            if (dataRequired.contains("STEPS")) {
                Optional<DataMeanDTO> data = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.STEPS));
                if (data.isPresent()) {
                    result.add(data.get());
                }
            }
            if (dataRequired.contains("SLEEP")) {
                Optional<DataMeanDTO> data = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.SLEEP));
                if (data.isPresent()) {
                    result.add(data.get());
                }
            }
            if (dataRequired.contains("ACTIVETIME")) {
                Optional<DataMeanDTO> data = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.ACTIVETIME));
                if (data.isPresent()) {
                    result.add(data.get());
                }
            }
            if (dataRequired.contains("SEATEDTIME")) {
                Optional<DataMeanDTO> data = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.SEATEDTIME));
                if (data.isPresent()) {
                    result.add(data.get());
                }
            }
            if (dataRequired.contains("BREATHINGRAWDATA")) {
                Optional<DataMeanDTO> data = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.BREATHINGRAWDATA));
                if (data.isPresent()) {
                    result.add(data.get());
                }
            }
            if (dataRequired.contains("BREATHINGPATTERN")) {
                Optional<DataMeanDTO> data = Optional.ofNullable(getMeanDataLastWeekForTypeData(mobileUser, DataType.BREATHINGPATTERN));
                if (data.isPresent()) {
                    result.add(data.get());
                }
            }
        }
        return result;
    }

    /*get the mean of last 7 days of one data type that belongs at  mobile user*/
    @Override
    public DataMeanDTO getMeanDataLastWeekForTypeData(MobileUser mobileUser, DataType type) {
        Optional<Set<Data>> data=dataRepository.findByMobileUserAndDataAndDateBetween(mobileUser,type,Instant.now().minus(7, ChronoUnit.DAYS),Instant.now());
        if(data.isPresent() && data.get().size()!=0) {
            log.debug("\n\nset data: "+data.get()+"\n");
            int i = 0;
            float media=0;
            Iterator<Data> iterator=data.get().iterator();
            while (iterator.hasNext()){
                Data object=iterator.next();
                try{
                    float f = Float.parseFloat(object.getValue());
                    media = media+ f;
                    i++;
                }catch (NumberFormatException ex){
                }
            }
            media=media/i;
            DataMeanDTO d;
            if (type == DataType.STEPS){
                d = new DataMeanDTO(type.name(),String.valueOf((int)media));
            }else{
                d = new DataMeanDTO(type.name(),String.valueOf(media));
            }
            return d;
        }
        return null;
    }

    /**
     * Delete the entity data.
     *
     * @param dataType the type of data that we want delete.
     * @param mobileUser to delete of mobile user too.
     */
    @Override
    public void deleteDataForType(MobileUser mobileUser, DataType dataType) {
        Optional<Set<Data>> data=dataRepository.findByMobileUserAndDataOrderByDateDesc(mobileUser,dataType);
        if(data.isPresent()){
            Iterator<Data> iterator=data.get().iterator();
            while(iterator.hasNext()){
                delete(iterator.next().getId());
            }
        }
    }

    /*this method returns the last data that you are add in your profile
     * of all types of data*/
    @Override
    public ArrayList<Data> getLastDataOfAllHealthData(MobileUser mobileUser) {
        ArrayList<Data> result= new ArrayList<>();
        Optional<Data> weight=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.WEIGHT);
        if(weight.isPresent()){
            result.add(weight.get());
        }
        Optional<Data> height=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.HEIGHT);
        if(height.isPresent()){
            result.add(height.get());
        }
        Optional<Data> systolic=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.SYSTOLIC);
        if(systolic.isPresent()){
            result.add(systolic.get());
        }
        Optional<Data> dyastolic=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.DYASTOLIC);
        if(dyastolic.isPresent()){
            result.add(dyastolic.get());
        }
        Optional<Data> seatedTime=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.SEATEDTIME);
        if(seatedTime.isPresent()){
            result.add(seatedTime.get());
        }
        Optional<Data> activeTime=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.ACTIVETIME);
        if(activeTime.isPresent()){
            result.add(activeTime.get());
        }
        Optional<Data> sleep=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.SLEEP);
        if(sleep.isPresent()){
            result.add(sleep.get());
        }
        Optional<Data> steps=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.STEPS);
        if(steps.isPresent()){
            result.add(steps.get());
        }
        Optional<Data> breathinGrawData=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.BREATHINGRAWDATA);
        if(breathinGrawData.isPresent()){
            result.add(breathinGrawData.get());
        }
        Optional<Data> breathsPerMinute=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.BREATHINGPATTERN);
        if(breathsPerMinute.isPresent()){
            result.add(breathsPerMinute.get());
        }
        return result;
    }

    /*this method returns the last data that you are add in your profile
     * of one type of data*/
    @Override
    public Optional<Data> getLastDataOfOneHealthData(MobileUser mobileUser, DataType dataType) {
        return dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,dataType);
    }

    /*this method returns the last data of data Type that belong at mobile user and were added between dategt and datelt*/
    @Override
    public Optional<Data> getTopByMobileUserAndDataAndDateBetweenOrderByDateDesc(MobileUser mobileUser, DataType dataType, Instant dateGT, Instant dateLT) {
        return dataRepository.findTopByMobileUserAndDataAndDateBetweenOrderByDateDesc(mobileUser,dataType,dateGT,dateLT);
    }

    /*this method returns the last data of data Type of each day between (dategt and datelt) that belong at mobile user */
    @Override
    public ResponseEntity<ArrayList<DataDTO>> getTopsByDataBetween(DataBetween dataBetween) {
        ArrayList<DataDTO> result=new ArrayList<>();
        int i=dataBetween.getDateGT().getDayOfYear()-dataBetween.getDateLT().getDayOfYear();
        int año=dataBetween.getDateGT().getYear()-dataBetween.getDateLT().getYear();
        if(año!=0){
            i=i+(365*año);
        }
        if(i<0){
            ResponseEntity error=new ResponseEntity("reverse dates, for example dateGT:2020-02-17 and dateLT:2020-02-12",HttpStatus.BAD_REQUEST);
            return error;
        }
        while(i!=-1){
            Instant dateMidnight=(LocalDateTime.of(dataBetween.getDateGT().minusDays(i), LocalTime.MIDNIGHT)).toInstant(ZoneId.of("Europe/Madrid").getRules().getOffset(LocalDateTime.of(dataBetween.getDateGT().minusDays(i), LocalTime.MIDNIGHT)));
            Instant dateMax=(LocalDateTime.of(dataBetween.getDateGT().minusDays(i), LocalTime.MAX)).toInstant(ZoneId.of("Europe/Madrid").getRules().getOffset(LocalDateTime.of(dataBetween.getDateGT().minusDays(i), LocalTime.MAX)));
            Optional<Data> data =dataRepository.findTopByMobileUserAndDataAndDateBetweenOrderByDateDesc(dataBetween.getMobileUser(),dataBetween.getData(),dateMidnight,dateMax);
            if(data.isPresent() && data.get()!=null){
                DataDTO dto= new DataDTO(data.get().getData(),data.get().getValue(),data.get().getDate());
                result.add(dto);
            }else{
                DataDTO d=new DataDTO(dataBetween.getData(),"0",dateMax);
                result.add(d);
            }
            i--;
        }
        Optional<ArrayList<DataDTO>> data= Optional.of(result);
        return ResponseUtil.wrapOrNotFound(data);
    }

}
