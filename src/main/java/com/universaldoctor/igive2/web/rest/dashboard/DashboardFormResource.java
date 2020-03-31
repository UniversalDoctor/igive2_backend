package com.universaldoctor.igive2.web.rest.dashboard;

import com.universaldoctor.igive2.domain.Form;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.User;
import com.universaldoctor.igive2.repository.*;
import com.universaldoctor.igive2.service.*;
import com.universaldoctor.igive2.service.dto.FormDTO;
import com.universaldoctor.igive2.service.dto.FormResult;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardFormResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "form";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final FormService formService;
    private final StudyService studyService;
    private final ResearcherRepository researcherRepository;

    public DashboardFormResource(UserService userService, FormService formService, StudyService studyService,
                                 ResearcherRepository researcherRepository) {
        this.userService = userService;
        this.formService = formService;
        this.studyService = studyService;
        this.researcherRepository = researcherRepository;
    }

    /**
     * {@code POST  /forms/{idStudy} : create and save a new Form
     * @param idStudy for associate the new form to one study
     * @param form the object to create.
     * @return the object
     */

    @PostMapping("/forms/{idStudy}")
    public ResponseEntity<Form> addForm(@RequestBody Form form, @PathVariable("idStudy") String idStudy) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to add invitation with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            Optional<Study> study=studyService.findOne(idStudy);
            if (researcher.isPresent() && study.isPresent()) {
                return ResponseUtil.wrapOrNotFound(formService.addAndSaveForm(study.get(),form,researcher.get()));
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * {@code POST  /form/Study/{idStudy} : create and save a new Form with the content of another  form
     * @param idStudy for associate the new form to one study
     * @param form the object to get the content.
     * @return the object
     */

   /* @PostMapping("/form/Study/{idStudy}")
    public ResponseEntity<Form> reportFormToAnotherStudy(@RequestBody Form form, @PathVariable("idStudy") String idStudy) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to add invitation with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            Optional<Study> study=studyService.findOne(idStudy);
            Optional<Form> optionalForm=formService.findOne(form.getId());
            if (researcher.isPresent() && study.isPresent() && optionalForm.isPresent()) {
                return ResponseUtil.wrapOrNotFound(formService.reportToAnotherStudy(optionalForm.get(),study.get()));
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }*/

    /**
     * {@code PUT /forms} : update the object
     *
     * @param form the object that I want update, updated
     * @return 200 and the object; 203 if the form or user are not present and 409 if the researcher is not present.
     */
    @PutMapping("/forms")
    public ResponseEntity<Form> updateForms(@RequestBody Form form) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent() ) {
                Optional<Form> optionalForm = formService.findOne(form.getId());
                if (optionalForm.isPresent() ){
                    if(optionalForm.get().getStudy().getResearcher().getId().equals(researcher.get().getId())) {
                        return ResponseUtil.wrapOrNotFound(formService.update(form,optionalForm.get()));
                    }
                }
                return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET /study/{idStudy}/forms} : get all the forms of one study
     *
     * @param idStudy the id of study that I want consult for get the forms
     * @return 200 and list of forms; 203 if the study or user are not present and 409 if the researcher is not present
     */
    @GetMapping("/study/{idStudy}/forms")
    public ResponseEntity<Set<Form>> getForms(@PathVariable("idStudy") String idStudy) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent() ) {
                Optional<Study> optionalStudy = studyService.findOne(idStudy);
                if (optionalStudy.isPresent() ){
                    if(optionalStudy.get().getResearcher().getId().equals(researcher.get().getId())) {
                        return ResponseUtil.wrapOrNotFound(formService.getForms(optionalStudy.get()));
                    }
                }
                return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET /study/{idStudy}/forms/info} : get all forms of one study but without the questions, and with most interesting information
     *
     * @param idStudy  the id of study that I want consult for get the forms
     * @return list of forms without questions and with number of questions and of answers
     */
    @GetMapping("/study/{idStudy}/forms/info")
    public ResponseEntity<ArrayList<FormDTO>> getAllForms(@PathVariable("idStudy") String idStudy) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent() ) {
                Optional<Study> optionalStudy = studyService.findOne(idStudy);
                if (optionalStudy.isPresent() ){
                    if(optionalStudy.get().getResearcher().getId().equals(researcher.get().getId())) {
                        Optional<ArrayList<FormDTO>> forms = Optional.ofNullable(formService.getFormsInfo(optionalStudy.get()));
                        return ResponseUtil.wrapOrNotFound(forms);
                    }
                }
                return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET /forms/{idForm}} : get one form of one study
     *
     * @param idForm the id of the form that I want get
     * @return form
     */
    @GetMapping("/forms/{idForm}")
    public ResponseEntity<Form> getForm(@PathVariable("idForm") String idForm) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent() ) {
                Optional<Form> form = formService.findOne(idForm);
                if (form.isPresent() ){
                    if(form.get().getStudy().getResearcher().getId().equals(researcher.get().getId())) {
                        return ResponseUtil.wrapOrNotFound(form);
                    }
                }
                return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }



    /**
     * {@code form/{idForm}/answers} : get the all answers of each participant of one form
     *
     * @param idForm the form to get the study and participants and responses
     * @return object form result, it have the information need of form and list of participants, every participant
     * have an anonymous id and another list of questions and answers
     */
    @GetMapping("form/{idForm}/answers")
    public ResponseEntity<FormResult> getResponses(@PathVariable("idForm") String idForm) {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                Optional<Form> optionalForm = formService.findOne(idForm);
                if (optionalForm.isPresent() && optionalForm.get().getStudy().getResearcher().getId().equals(researcher.get().getId())) {
                    Optional<FormResult> result = Optional.ofNullable(formService.getFormResult(optionalForm.get()));
                    return ResponseUtil.wrapOrNotFound(result);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }


    /**
     * {@code DELETE  "/forms/{formId}"} : delete the "form"
     *
     * @param formId the id of the form that I want delete
     * @return the form that I have deleted
     */
    @DeleteMapping("/forms/{formId}")
    public ResponseEntity<Form> removeForms(@PathVariable("formId") String formId) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> ruser = userService.checkAuthoritation();
        if (ruser.isPresent()) {
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(ruser.get().getId());
            if (researcher.isPresent()) {
                return ResponseUtil.wrapOrNotFound(formService.deleteForm(formId,researcher.get()));
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
