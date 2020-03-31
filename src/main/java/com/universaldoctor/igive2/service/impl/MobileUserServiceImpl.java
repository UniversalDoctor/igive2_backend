package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.DataType;
import com.universaldoctor.igive2.domain.enumeration.Diseases;
import com.universaldoctor.igive2.domain.enumeration.GenderType;
import com.universaldoctor.igive2.repository.DataRepository;
import com.universaldoctor.igive2.repository.IGive2UserRepository;
import com.universaldoctor.igive2.repository.UserRepository;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.MobileUserService;
import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.repository.MobileUserRepository;
import com.universaldoctor.igive2.service.dto.Profile;
import com.universaldoctor.igive2.service.dto.SetUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link MobileUser}.
 */
@Service
public class MobileUserServiceImpl implements MobileUserService {

    private final Logger log = LoggerFactory.getLogger(MobileUserServiceImpl.class);

    private final MobileUserRepository mobileUserRepository;

    private final IGive2UserService iGive2UserService;
    private final DataRepository dataRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final DataService dataService;
    private final ParticipantService participantService;

    public MobileUserServiceImpl(MobileUserRepository mobileUserRepository, IGive2UserService iGive2UserService,
                                 DataRepository dataRepository, UserService userService, UserRepository userRepository,
                                 DataService dataService, ParticipantService participantService) {
        this.mobileUserRepository = mobileUserRepository;
        this.iGive2UserService = iGive2UserService;
        this.dataRepository = dataRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.dataService = dataService;
        this.participantService = participantService;
    }

    /**
     * Save a mobileUser.
     *
     * @param mobileUser the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MobileUser save(MobileUser mobileUser) {
        log.debug("Request to save MobileUser : {}", mobileUser);
        return mobileUserRepository.save(mobileUser);
    }

    /**
     * Get all the mobileUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<MobileUser> findAll(Pageable pageable) {
        log.debug("Request to get all MobileUsers");
        return mobileUserRepository.findAll(pageable);
    }


    /**
     * Get one mobileUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<MobileUser> findOne(String id) {
        log.debug("Request to get MobileUser : {}", id);
        return mobileUserRepository.findById(id);
    }

    /**
     * Delete the mobileUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete MobileUser : {}", id);
        mobileUserRepository.deleteById(id);
    }

    /**
     * Delete the mobileUser by entity.
     *
     * @param mobileUser the entity.
     */
    @Override
    public void deleteProfile(MobileUser mobileUser) {
        Optional<IGive2User> gu = Optional.ofNullable(mobileUser.getIGive2User());
        if (gu.isPresent()) {
            iGive2UserService.delete(gu.get().getId());
        }
        dataService.deleteAllData(mobileUser);
        participantService.deleteAllParticipation(mobileUser);
        userService.deleteUser(userRepository.findById(mobileUser.getUserId()).get().getLogin());
        delete(mobileUser.getId());
    }

    /**
     * update the mobileUser by entity.
     * @param user the entity.
     * @param mobileUser the new entity.
     * @return  the entity
     */
    @Override
    public Optional<MobileUser> update(MobileUser user,MobileUser mobileUser) {
        if(mobileUser.getDiseases()!=null){
            user.setDiseases(mobileUser.getDiseases());
        }
        if(mobileUser.getBirthdate()!=null) {
            user.setBirthdate(mobileUser.getBirthdate());
        }
        if(mobileUser.getGender()!=null) {
            user.setGender(mobileUser.getGender());
        }
        save(user);
        Optional<MobileUser> optionalMobileUser=findOne(user.getId());
        return  optionalMobileUser;
    }

    /**
     * create one participant by mobile user and study.
     *
     * @param iGive2User the id of the entity.
     * @param userId the id of the entity.
     * @return the entity.
     */
    @Override
    public MobileUser create(IGive2User iGive2User, String userId) {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setGender(GenderType.OTHER);
        mobileUser.setDiseases(Diseases.NONE);
        mobileUser.setIGive2User(iGive2User);
        mobileUser.setUserId(userId);
        save(mobileUser);
        return mobileUser;
    }

    @Override
    public void setUp(MobileUser mobileUser,SetUp setUp) {
        mobileUser.setGender(setUp.getGender());
        mobileUser.setBirthdate(setUp.getBirthdate());
        mobileUser.setDiseases(setUp.getDiseases());
        save(mobileUser);
        Optional<IGive2User> iGive2User= iGive2UserService.findOne(mobileUser.getIGive2User().getId());
        if(iGive2User.isPresent()){
            iGive2User.get().setCountry(setUp.getCountry());
            iGive2UserService.save(iGive2User.get());
        }
        Optional<Data> originalWeight=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.WEIGHT);
        if((originalWeight.isPresent() && !originalWeight.get().getValue().equals(setUp.getWeight())) || !originalWeight.isPresent()) {
            Data weight = new Data();
            weight.setValue(setUp.getWeight());
            weight.setData(DataType.WEIGHT);
            dataService.putData(weight, mobileUser);
        }
        Optional<Data> originalHeight=dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.HEIGHT);
        if((originalHeight.isPresent() && !originalHeight.get().getValue().equals(setUp.getHeight())) || !originalHeight.isPresent() ){
            log.debug("\n\n height: "+setUp.getHeight()+"\n");
            Data height=new Data();
            height.setValue(setUp.getHeight());
            height.setData(DataType.HEIGHT);
            dataService.putData(height,mobileUser);
        }
    }

    /**
     *  get the mobile profile with setup form .
     *
     * @param mobileUser the entity.
     * @return mobile profile with setup form.
     */
    @Override
    public SetUp getSetUp(MobileUser mobileUser) {
        int numStudies=0;
        Optional<User> user=userRepository.findById(mobileUser.getUserId());
        Optional<IGive2User> iGive2User= iGive2UserService.findOne(mobileUser.getIGive2User().getId());
        Optional<Set<Participant>> participant= participantService.getFromMobileUser(mobileUser);
        Optional<Data> weight= dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.WEIGHT);
        Optional<Data> height= dataRepository.findTopByMobileUserAndDataOrderByDateDesc(mobileUser,DataType.HEIGHT);
        if(participant.isPresent() && participant.get()!=null && !participant.get().isEmpty()){
            numStudies=participant.get().size();
        }
        if(iGive2User.isPresent() && user.isPresent()) {
            log.debug("\n\nuser first and last name: "+user.get().getFirstName()+", "+user.get().getLastName()+"\n");
            if(weight.isPresent() && height.isPresent()) {
                SetUp result = new SetUp(user.get().getLogin(),user.get().getFirstName(),
                    user.get().getLastName(),user.get().getEmail(),iGive2User.get().getCountry(), mobileUser.getGender(),
                    mobileUser.getBirthdate(), mobileUser.getDiseases(), weight.get().getValue(), height.get().getValue(),
                    numStudies,iGive2User.get().isNewsletter());
                return result;
            }
            if(weight.isPresent()){
                SetUp result = new SetUp(user.get().getLogin(),user.get().getFirstName(),
                    user.get().getLastName(),user.get().getEmail(),iGive2User.get().getCountry(), mobileUser.getGender(),
                    mobileUser.getBirthdate(), mobileUser.getDiseases(), weight.get().getValue(), null,
                    numStudies,iGive2User.get().isNewsletter());
                return result;
            }
            if(height.isPresent()) {
                SetUp result = new SetUp(user.get().getLogin(),user.get().getFirstName(),
                    user.get().getLastName(),user.get().getEmail(),iGive2User.get().getCountry(), mobileUser.getGender(),
                    mobileUser.getBirthdate(), mobileUser.getDiseases(), null, height.get().getValue(),
                    numStudies,iGive2User.get().isNewsletter());
                return result;
            }
            SetUp result = new SetUp(user.get().getLogin(),user.get().getFirstName(),
                user.get().getLastName(),user.get().getEmail(),iGive2User.get().getCountry(), mobileUser.getGender(),
                mobileUser.getBirthdate(), mobileUser.getDiseases(), null,null,
                numStudies,iGive2User.get().isNewsletter());
            return result;
        }
        return  null;
    }

    /**
     * update profile of mobileuser (profile= username, icon, status)
     *
     * @param mobileUser the entity.
     * @param profile the change the information.
     */
    @Override
    public void setProfile(MobileUser mobileUser, Profile profile) {
        mobileUser.setUsername(profile.getUsername());
        mobileUser.setIcon(profile.getIcon());
        mobileUser.setStatus(profile.getStatus());
        save(mobileUser);
    }

    /**
     *  get profile of mobileuser (profile= username, icon, status)
     *
     * @param mobileUser the entity to get the information.
     * @return (profile= username, icon, status)
     */
    @Override
    public Profile getProfile(MobileUser mobileUser) {
        Optional<User> user= userRepository.findById(mobileUser.getUserId());
        if(user.isPresent() && user.get()!=null){
            Profile profile=new Profile(mobileUser.getIcon(),mobileUser.getUsername(),user.get().getEmail(),mobileUser.getStatus());
            return profile;
        }
        return  null;
    }
}
