package com.universaldoctor.igive2.web.rest.mobile;

import com.universaldoctor.igive2.domain.Data;
import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.Participant;
import com.universaldoctor.igive2.domain.User;
import com.universaldoctor.igive2.domain.enumeration.DataType;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.service.*;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/mobile")
public class AppParticipantDataResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "participantData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataService dataService;
    private final ParticipantService participantService;
    private final UserService userService;
    private final MobileUserRepository mobileUserRepository;

    public AppParticipantDataResource(DataService dataService, UserService userService, ParticipantService participantService,MobileUserRepository mobileUserRepository) {
        this.dataService = dataService;
        this.userService = userService;
        this.participantService=participantService;
        this.mobileUserRepository = mobileUserRepository;
    }

    /**
     * {@code POST /data/participant/{data}} : add all data of one type in participant if type of data are in requested data of study
     *
     * @param data the type of data that I want to add
     * @param participant the participant where I add the data, only need put the id of participant to get it.
     * @return participant
     */
    @PostMapping("/data/participant/{data}")
    public ResponseEntity<Participant> addDataParticipant(@RequestBody Participant participant, @PathVariable("data") DataType data) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if(user.isPresent()) {
            log.debug("REST request to add Data in the MobileUser : {}", user.get().getLogin());
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            Optional<Participant> optionalParticipant= participantService.findOne(participant.getId());
            if (mu.isPresent() && optionalParticipant.isPresent()) {
                return dataService.putHealthDataInParticipant(data,optionalParticipant.get(),mu.get());
            }

            ResponseEntity error = new ResponseEntity("the object does not correspond to any participant of this profile, " +
                "try again with another body, can be a problem that mobileuser is not present too", HttpStatus.BAD_REQUEST);
            return error;
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    /**
     * {@code POST /data/participant/one/{idData}} : add one data in participant if type of data are in requested data of study
     *
     * @param idData the id of data that I want to add
     * @param participant the participant where I add the data, only need put the id of participant to get it.
     * @return data
     */
    @PostMapping("/data/participant/one/{idData}")
    public ResponseEntity<Data> addOneDataParticipant(@RequestBody Participant participant, @PathVariable("idData") String idData) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if(user.isPresent()) {
            log.debug("REST request to add Data in the MobileUser : {}", user.get().getLogin());
            Optional<Data> data=dataService.findOne(idData);
            Optional<Participant> optionalParticipant= participantService.findOne(participant.getId());
            if (data.isPresent() && optionalParticipant.isPresent()) {
                return ResponseUtil.wrapOrNotFound(dataService.putOneDataInParticipant(data.get(),optionalParticipant.get()));
            }

            ResponseEntity error = new ResponseEntity("the object does not correspond to any participant of this profile, " +
                "try again with another body, can be a problem that mobileuser is not present too", HttpStatus.BAD_REQUEST);
            return error;
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code GET /data/participant/{idParticipant}} : get all data of the participant
     *
     * @param idParticipant the object for I can get the data, only can put the id of it.
     * @return list of data
     */
    @GetMapping("/data/participant/{idParticipant}")
    public ResponseEntity<Set<Data>> getAllDataParticipant(@PathVariable ("idParticipant") String idParticipant) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if(user.isPresent()) {
            log.debug("REST request to add Data in the MobileUser : {}", user.get().getLogin());
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            Optional<Participant> optionalParticipant= participantService.findOne(idParticipant);
            if (mu.isPresent() && optionalParticipant.isPresent() && optionalParticipant.get()!=null) {
                Optional<Set<Data>> result= Optional.ofNullable(optionalParticipant.get().getParticipantData());
                return ResponseUtil.wrapOrNotFound(result);
            }
            ResponseEntity error = new ResponseEntity("the object does not correspond to any participant of this profile, " +
                "try again with another body, can be a problem that mobileuser is not present too",HttpStatus.BAD_REQUEST);
            return error;
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code DELETE  /data/participant/{idParticipant} : delete all the healthData of  participant
     *
     * @param idParticipant to get the data that I want delete, only can put the id of participant in the body.
     * @return participant
     */
    @DeleteMapping("/data/participant/{idParticipant}")
    public ResponseEntity<Participant> deleteDataParticipant(@PathVariable("idParticipant") String idParticipant) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()){
            log.debug("REST request to delete healthData to MobileUser : {}", user.get().getLogin());
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            Optional<Participant> participant = participantService.findOne(idParticipant);
            if (mu.isPresent() && participant.isPresent()) {
                return dataService.deleteHealthDataInParticipant(participant.get(), mu.get());
            }
            ResponseEntity error=new ResponseEntity("mobile user or participant are not present, try again with another" +
                " id",HttpStatus.BAD_REQUEST);
            return error;
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
