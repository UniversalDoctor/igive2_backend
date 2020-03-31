package com.universaldoctor.igive2.web.rest.mobile;

import com.universaldoctor.igive2.domain.Data;
import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.domain.User;
import com.universaldoctor.igive2.domain.enumeration.DataType;
import com.universaldoctor.igive2.repository.MobileUserRepository;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.dto.DataBetween;
import com.universaldoctor.igive2.service.dto.DataDTO;
import com.universaldoctor.igive2.service.dto.HypertensionDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/mobile")
public class AppDataResource {

    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "data";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataService dataService;
    private final UserService userService;
    private final MobileUserRepository mobileUserRepository;

    public AppDataResource(DataService dataService, UserService userService, MobileUserRepository mobileUserRepository) {

        this.dataService = dataService;
        this.userService = userService;
        this.mobileUserRepository = mobileUserRepository;
    }

    /**
     * {@code POST /healthData} : from a data object,then added it in list of health data of mobile user and saved it in the database
     *
     * @param healthData the data object, only need the value and the type of data to save it
     * @return the data that you are added
     */
    @PostMapping("/healthData")
    public ResponseEntity<Data> addHealthData(@Valid @RequestBody Data healthData) throws URISyntaxException {
        Optional<User> user =userService.checkAuthoritation();
        log.debug("add data in mobile user: "+user.get().getLogin());
        Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
        if (user.isPresent() && mu.isPresent()) {
            return ResponseUtil.wrapOrNotFound(dataService.putData(healthData,mu.get()));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code POST /healthData/hypertension} : from a systolic and dyastolic values, created a two data objects related with mobile user and saved it in database
     *
     * @param hypertensionDTO the object with values
     * @return two data objects
     */
    @PostMapping("/healthData/hypertension")
    public ResponseEntity<Set<Data>> addHypertensionData(@Valid @RequestBody HypertensionDTO hypertensionDTO) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> ruser =userService.checkAuthoritation();
        Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(ruser.get().getId());
        if (ruser.isPresent() && mu.isPresent()) {
            Set<Data> data = dataService.putHypertensionData(hypertensionDTO,mu.get());
            return ResponseEntity.ok().body(data);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code GET /healthData} : get all data from the mobile user
     *
     * @return all data of mobileUser
     */
    @GetMapping("/healthData")
    public ResponseEntity<Set<Data>> getData() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> ruser = userService.checkAuthoritation();
        if (ruser.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(ruser.get().getId());
            if (mobileUser.isPresent()) {
                return ResponseUtil.wrapOrNotFound(dataService.getData(mobileUser.get()));
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code GET /healthData/{dataType}} : get the last data that are you added the type of dataType
     *
     * @param dataType the type of the data
     * @return last data of mobileUser of dataType.
     */
    @GetMapping("/healthData/{dataType}")
    public ResponseEntity<Data> getTopData(@PathVariable("dataType") DataType dataType) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> ruser = userService.checkAuthoritation();
        if (ruser.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(ruser.get().getId());
            if (mobileUser.isPresent()) {
                Optional<Data> data =dataService.getLastDataOfOneHealthData(mobileUser.get(),dataType);
                return ResponseUtil.wrapOrNotFound(data);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code POST /healthData/dataBetween} : add the last data of data Type of each day between (dateGT and dateLT) that belong at mobile user in a new set of data
     *
     * @param dataBetween in this class are the mobile user, the type of data that i want find and the period of time which i want find this data
     * @return a list of data of data type between (dateGT and dateLT).
     */
    @PostMapping("/healthData/dataBetween")
    public ResponseEntity<ArrayList<DataDTO>> getTopDataOfDataTypeBetween2Days(@RequestBody DataBetween dataBetween) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> ruser = userService.checkAuthoritation();
        if (ruser.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(ruser.get().getId());
            if (mobileUser.isPresent()) {
                dataBetween.setMobileUser(mobileUser.get());
                return dataService.getTopsByDataBetween(dataBetween);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code GET /data} : get the data of each type from mobile user
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @GetMapping("/data")
    public ResponseEntity<ArrayList<Data>> getLastData() throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> ruser = userService.checkAuthoritation();
        if (ruser.isPresent()) {
            Optional<MobileUser> mobileUser = mobileUserRepository.findOneByUserId(ruser.get().getId());
            if (mobileUser.isPresent()) {
                Optional<ArrayList<Data>> data = Optional.ofNullable(dataService.getLastDataOfAllHealthData(mobileUser.get()));
                return ResponseUtil.wrapOrNotFound(data);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code DELETE  /healthData/{idData}} : delete data that are referenced with the id, but only if it belongs to mobile user
     *
     * @param idData for can get the data object
     * @return true if removed correctly
     */
    @DeleteMapping("/healthData/{idData}")
    public ResponseEntity<Boolean> deleteOneData( @PathVariable String idData) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to delete healthData to MobileUser : {}", user.get().getLogin());
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            Optional<Data> data=dataService.findOne(idData);
            if(data.isPresent() && data.get().getMobileUser().getId().equals(data.get().getMobileUser().getId())) {
                dataService.delete(data.get().getId());
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            ResponseEntity error=new ResponseEntity("the id does not correspond to any data of this profile or the gate are wrong",HttpStatus.BAD_REQUEST);
            return error;
        }
        return new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);

    }

    /**
     * {@code DELETE  /data/{dataType}} :delete all data of that type belonging to the mobile user
     *
     * @param dataType the type of data that i want delete
     * @return true if removed correctly
     */
    @DeleteMapping("/data/{dataType}")
    public ResponseEntity<Boolean> deleteDataForType( @PathVariable DataType dataType) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to delete healthData to MobileUser : {}", user.get().getLogin());
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            if(mu.isPresent() ) {
                dataService.deleteDataForType(mu.get(),dataType);
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            ResponseEntity error=new ResponseEntity("there is not or does not recognize any mobile user",HttpStatus.CONFLICT);
            return error;
        }
        return new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);

    }

    /**
     * {@code DELETE  /healthData} : delete all data of mobile user
     *
     * @return true if removed correctly
     */
    @DeleteMapping("/healthData")
    public ResponseEntity<Boolean> deleteAllData( ) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to delete healthData to MobileUser : {}", user.get().getLogin());
            Optional<MobileUser> mu = mobileUserRepository.findOneByUserId(user.get().getId());
            if(mu.isPresent()) {
                dataService.deleteAllData(mu.get());
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            ResponseEntity error=new ResponseEntity("there is not or does not recognize any mobile user",HttpStatus.CONFLICT);
            return error;
        }
        return new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);

    }


}
