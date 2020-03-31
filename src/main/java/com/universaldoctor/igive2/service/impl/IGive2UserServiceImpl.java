package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.MobileUser;
import com.universaldoctor.igive2.service.IGive2UserService;
import com.universaldoctor.igive2.domain.IGive2User;
import com.universaldoctor.igive2.repository.IGive2UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link IGive2User}.
 */
@Service
public class IGive2UserServiceImpl implements IGive2UserService {

    private final Logger log = LoggerFactory.getLogger(IGive2UserServiceImpl.class);

    private final IGive2UserRepository iGive2UserRepository;

    public IGive2UserServiceImpl(IGive2UserRepository iGive2UserRepository) {
        this.iGive2UserRepository = iGive2UserRepository;
    }

    /**
     * Save a iGive2User.
     *
     * @param iGive2User the entity to save.
     * @return the persisted entity.
     */
    @Override
    public IGive2User save(IGive2User iGive2User) {
        log.debug("Request to save IGive2User : {}", iGive2User);
        return iGive2UserRepository.save(iGive2User);
    }

    /**
     * Get all the iGive2Users.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<IGive2User> findAll(Pageable pageable) {
        log.debug("Request to get all IGive2Users");
        return iGive2UserRepository.findAll(pageable);
    }



    /**
     * Get one iGive2User by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<IGive2User> findOne(String id) {
        log.debug("Request to get IGive2User : {}", id);
        return iGive2UserRepository.findById(id);
    }

    /**
     * Delete the iGive2User by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete IGive2User : {}", id);
        iGive2UserRepository.deleteById(id);
    }


     /**
     * create a Igive2user.
     *
     * @param newsLetter part of body.
     * @param country part of body.
     * @return the persisted entity.
     */
    @Override
    public IGive2User create(boolean newsLetter,String country) {
        IGive2User iGive2User = new IGive2User();
        iGive2User.setTermsAccepted(true);
        iGive2User.setNewsletter(newsLetter);
        iGive2User.setCountry(country);
        save(iGive2User);
        return  iGive2User;
    }


}
