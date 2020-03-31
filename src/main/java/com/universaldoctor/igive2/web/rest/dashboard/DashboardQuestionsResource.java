package com.universaldoctor.igive2.web.rest.dashboard;

import com.universaldoctor.igive2.domain.Form;
import com.universaldoctor.igive2.domain.FormQuestion;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.User;
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
@RequestMapping("/api/dashboard")
public class DashboardQuestionsResource {

    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "questions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final FormService formService;
    private final ResearcherRepository researcherRepository;
    private final FormQuestionService formQuestionService;

    public DashboardQuestionsResource(UserService userService, FormService formService, ResearcherRepository researcherRepository,
                                      FormQuestionService formQuestionService) {
        this.userService = userService;
        this.formService = formService;
        this.researcherRepository = researcherRepository;
        this.formQuestionService = formQuestionService;
    }

    /**
     * {@code POST  /forms/questions/{idForm}} : create and save a new question
     *
     * @param formQuestion the object that I want create and save
     * @param idForm the id of form where belong the new question
     * @return the object
     */

    @PostMapping("/forms/questions/{idForm}")
    public ResponseEntity<FormQuestion> addFormQuestion(@RequestBody FormQuestion formQuestion, @PathVariable ("idForm") String idForm) throws URISyntaxException {
        Optional<User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to add invitation with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            Optional<Form> form=formService.findOne(idForm);
            if (researcher.isPresent() && form.isPresent()) {
                return ResponseUtil.wrapOrNotFound(formQuestionService.addSaveNewFormQuestion(form.get(),researcher.get(),formQuestion));
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * {@code PUT  /forms/questions} : update one question
     *
     * @param formQuestion the object that I want update, updated
     * @return the object updated.
     */

    @PutMapping("/forms/questions")
    public ResponseEntity<FormQuestion> updateFormQuestion(@RequestBody FormQuestion formQuestion) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to add invitation with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            Optional<FormQuestion> form=formQuestionService.findOne(formQuestion.getId());
            if (researcher.isPresent() && form.isPresent() && form.get().getForm().getStudy().getResearcher().getId().equals(researcher.get().getId())) {
                return ResponseUtil.wrapOrNotFound(formQuestionService.update(formQuestion,form.get()));
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * {@code GET /forms/{idForm}/questions} : get all the questions of one form
     *
     * @param idForm the id of form that I want consult, for get the questions
     * @return list of questions that belong at form
     */
    @GetMapping("/forms/{idForm}/questions")
    public ResponseEntity<Set<FormQuestion>> getFormsQuestions(@PathVariable ("idForm") String idForm) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                Optional<Form> optionalForm = formService.findOne(idForm);
                if (optionalForm.isPresent() && optionalForm.get().getStudy().getResearcher().getId().equals(researcher.get().getId())) {
                    return ResponseUtil.wrapOrNotFound(formQuestionService.getFromFom(optionalForm.get()));
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code GET /questions/{questionId}} : get one questions of one form
     *
     * @param questionId the id of question that I want get
     * @return the object
     */
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<FormQuestion> getFormQuestion(@PathVariable ("questionId") String questionId) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            log.debug("REST request to get institutions with researcher : {}", user.get().getLogin());
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            if (researcher.isPresent()) {
                Optional<FormQuestion> question = formQuestionService.findOne(questionId);
                if (question.isPresent() && question.get().getForm().getStudy().getResearcher().getId().equals(researcher.get().getId())) {
                    return ResponseUtil.wrapOrNotFound(question);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * {@code DELETE  "/forms/questions/{formQuestionId}"} : delete the question
     *
     * @param formQuestionId the id of question that I want delete
     * @return the question that I was deleted
     */

    @DeleteMapping("/forms/questions/{formQuestionId}")
    public ResponseEntity<FormQuestion> removeFormQuestion(@PathVariable("formQuestionId") String formQuestionId) throws URISyntaxException {
        Optional<com.universaldoctor.igive2.domain.User> user = userService.checkAuthoritation();
        if (user.isPresent()) {
            Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
            Optional<FormQuestion> formQuestion=formQuestionService.findOne(formQuestionId);
            if (researcher.isPresent() && formQuestion.isPresent()) {
                return ResponseUtil.wrapOrNotFound(formQuestionService.deleteFormQuestion(formQuestionId,researcher.get()));
            }
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
