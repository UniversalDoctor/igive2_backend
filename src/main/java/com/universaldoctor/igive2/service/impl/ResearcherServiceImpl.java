package com.universaldoctor.igive2.service.impl;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.domain.enumeration.State;
import com.universaldoctor.igive2.repository.ParticipantInvitationRepository;
import com.universaldoctor.igive2.repository.UserRepository;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.ResearcherService;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.repository.ResearcherRepository;
import com.universaldoctor.igive2.service.dto.DashboardProfile;
import com.universaldoctor.igive2.service.dto.MailDTO;
import com.universaldoctor.igive2.service.dto.ManagedUserVM;
import com.universaldoctor.igive2.service.dto.ResearcherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Researcher}.
 */
@Service
public class ResearcherServiceImpl implements ResearcherService {

    private final Logger log = LoggerFactory.getLogger(ResearcherServiceImpl.class);

    private static final String STUDY = "study";

    private static final String BASE_URL = "baseUrl";

    private final ResearcherRepository researcherRepository;

    private final IGive2UserService iGive2UserService;
    private final JavaMailSender javaMailSender;
    private final ParticipantInvitationRepository participantInvitationRepository;
    private final StudyService studyService;
    private final UserService userService;
    private final UserRepository userRepository;

    public ResearcherServiceImpl(ResearcherRepository researcherRepository, IGive2UserService iGive2UserService, JavaMailSender javaMailSender, ParticipantInvitationRepository participantInvitationRepository, StudyService studyService, UserService userService, UserRepository userRepository) {
        this.researcherRepository = researcherRepository;
        this.iGive2UserService = iGive2UserService;
        this.javaMailSender = javaMailSender;
        this.participantInvitationRepository = participantInvitationRepository;
        this.studyService = studyService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Save a researcher.
     *
     * @param researcher the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Researcher save(Researcher researcher) {
        log.debug("Request to save Researcher : {}", researcher);
        return researcherRepository.save(researcher);
    }

    /**
     * Get all the researchers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Researcher> findAll(Pageable pageable) {
        log.debug("Request to get all Researchers");
        return researcherRepository.findAll(pageable);
    }


    /**
     * Get one researcher by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Researcher> findOne(String id) {
        log.debug("Request to get Researcher : {}", id);
        return researcherRepository.findById(id);
    }

    /**
     * Delete the researcher by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Researcher : {}", id);
        researcherRepository.deleteById(id);
    }

    /**
     * Get the profile of researcher with information of user and igive2user too.
     *
     * @param researcher the object to get the information.
     * @return the entity.
     */
    @Override
    public DashboardProfile getResearcher(Researcher researcher) {
        Optional<User> user=userRepository.findById(researcher.getUserId());
        Optional<IGive2User> iGive2User= iGive2UserService.findOne(researcher.getIGive2User().getId());
        if(user.isPresent() && iGive2User.isPresent()){
            DashboardProfile dashboardProfile=new DashboardProfile(user.get().getFirstName(),user.get().getLastName(),researcher.getHonorifics(),user.get().getEmail(), researcher.getInstitution(),
                iGive2User.get().getCountry(),studyService.getStateStudies(researcher, State.PUBLISHED));
            return dashboardProfile;
        }
        return  null;
    }

    /**
     * Update the profile of researcher with information of user and igive2user too.
     *
     * @param dashboardProfile the object to get the information that I want update.
     * @param id of the researcher for can updated it
     * @return the entity.
     */
    @Override
    public DashboardProfile setResearcher(DashboardProfile dashboardProfile, String id) {
        Optional<Researcher> researcher=researcherRepository.findById(id);
        Optional<User> user=userRepository.findById(researcher.get().getUserId());
        log.debug("\n\nset researcher: "+user.get().getLogin()+"\n");
        Optional<IGive2User> iGive2User= iGive2UserService.findOne(researcher.get().getIGive2User().getId());
        if(user.isPresent() && iGive2User.isPresent() && researcher.isPresent()){
            if (dashboardProfile.getFirstName()!=null ) {
                user.get().setFirstName(dashboardProfile.getFirstName());
            }
            if (dashboardProfile.getLastName()!=null ) {
                user.get().setLastName(dashboardProfile.getLastName());
            }
            userRepository.save(user.get());
            if (dashboardProfile.getCountry()!=null) {
                iGive2User.get().setCountry(dashboardProfile.getCountry());
                iGive2UserService.save(iGive2User.get());
            }
            if(dashboardProfile.getInstitution()!=null){
                researcher.get().setInstitution(dashboardProfile.getInstitution());
            }
            if(dashboardProfile.getTitle()!=null){
                researcher.get().setHonorifics(dashboardProfile.getTitle());
            }
            researcherRepository.save(researcher.get());
        }
        return  null;
    }

    /**
     * Sen an email to all participantsç
     *
     * @param mail the object to get the information of participants, subject and body of mail
     *
     */
    @Override
    public void sendMailAllParticipants(MailDTO mail) {
        Optional<Study> study=studyService.findOne(mail.getStudyId());
        if(study.isPresent()) {
            Optional<Set<ParticipantInvitation>> participantInvitation = participantInvitationRepository.findByStudyAndStateOrderByIdAsc(study.get(),true);
            if(participantInvitation.isPresent()){
                String [] destinatarios= getDestinatarios(participantInvitation.get());
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                try {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
                    message.setBcc(destinatarios);
                    message.setFrom(mail.getResearcherMail());
                    message.setSubject(mail.getSubject());
                    message.setText(mail.getBody());
                    javaMailSender.send(mimeMessage);
                }  catch (MailException | MessagingException e) {
                    log.error("Email could not be sent to user '{}'", e);
                }
            }
        }
    }

    /*From a list of invitations that are accepted returns an array with the emails of participants*/
    @Override
    public String[] getDestinatarios(Set<ParticipantInvitation> participantInvitation) {
        String [] emails= new String[participantInvitation.size()];
        int i=0;
        Iterator<ParticipantInvitation> iterator=participantInvitation.iterator();
        while (iterator.hasNext()){
            emails[i]=iterator.next().getEmail();
            i++;
        }
        return emails;
    }



    /*from ResearcherDTO create and returns a ManagedUserVM , is the information that you need for the registration*/
    @Override
    public ManagedUserVM passOfResearcherDTOtoManagedUser(ResearcherDTO researcherDTO) {
        ManagedUserVM managedUserVM=new ManagedUserVM();
        managedUserVM.setPassword(researcherDTO.getPassword());
        managedUserVM.setActivated(researcherDTO.isActivated());
        managedUserVM.setAuthorities(researcherDTO.getAuthorities());
        managedUserVM.setCreatedBy(researcherDTO.getCreatedBy());
        managedUserVM.setCreatedDate(researcherDTO.getCreatedDate());
        managedUserVM.setEmail(researcherDTO.getEmail());
        managedUserVM.setFirstName(researcherDTO.getFirstName());
        managedUserVM.setLastName(researcherDTO.getLastName());
        managedUserVM.setId(researcherDTO.getId());
        managedUserVM.setImageUrl(researcherDTO.getImageUrl());
        managedUserVM.setLangKey(researcherDTO.getLangKey());
        managedUserVM.setLastModifiedBy(researcherDTO.getLastModifiedBy());
        managedUserVM.setLastModifiedDate(researcherDTO.getLastModifiedDate());
        return managedUserVM;
    }

    /*create and save a researcher with the features institution, honorifics, userId and iGive2User*/
    @Override
    public Researcher create(String institution, String honorifics, String userId, IGive2User iGive2User) {
        Researcher researcher = new Researcher();
        researcher.setHonorifics(honorifics);
        researcher.setInstitution(institution);
        researcher.setUserId(userId);
        researcher.setIGive2User(iGive2User);
        save(researcher);
        return researcher;
    }


    /**
     * Delete the entity researcher.
     *
     * @param researcher the entity.
     */
    @Override
    public void deleteResearcher(Researcher researcher) {
        Optional<IGive2User> gu = Optional.ofNullable(researcher.getIGive2User());
        if (gu.isPresent()) {
            iGive2UserService.delete(gu.get().getId());
        }
        Optional<Set<Study>> studies=studyService.getStudyByResearcher(researcher);
        if (studies.isPresent() && studies.get()!=null && !studies.get().isEmpty()) {
            Iterator<Study> iterator = studies.get().iterator();
            while (iterator.hasNext()) {
                studyService.deleteStudy(iterator.next(),researcher);
            }
        }
        userService.deleteUser(userRepository.findById(researcher.getUserId()).get().getLogin());
        delete(researcher.getId());
    }
}
