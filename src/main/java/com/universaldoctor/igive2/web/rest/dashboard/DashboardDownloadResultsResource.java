package com.universaldoctor.igive2.web.rest.dashboard;

import com.universaldoctor.igive2.domain.*;
import com.universaldoctor.igive2.repository.ResearcherRepository;
import com.universaldoctor.igive2.service.*;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardDownloadResultsResource {
    private final Logger log = LoggerFactory.getLogger(com.universaldoctor.igive2.web.rest.MobileUserResource.class);

    private static final String ENTITY_NAME = "download";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final StudyService studyService;
    private final ParticipantService participantService;
    private final DataService dataService;
    private final ResearcherRepository researcherRepository;
    private final FormService formService;
    private final ServiceCSV serviceCSV;

    public DashboardDownloadResultsResource(UserService userService, StudyService studyService, ParticipantService participantService, DataService dataService, ResearcherRepository researcherRepository, FormService formService) {
        this.userService = userService;
        this.studyService = studyService;
        this.participantService = participantService;
        this.dataService = dataService;
        this.researcherRepository = researcherRepository;
        this.formService = formService;
        this.serviceCSV = new ServiceCSV(formService,dataService,participantService);
    }

    /**
     * {@code GET /download/form/{formId}: get a folder with file.csv inside, the file.csv contains all the data of the form
     *
     * @param formId the id of form that with it I want create a file with all his data
     * @return the path of the folder
     * @return array of bytes
     */
    @GetMapping("/download/form/{formId}")
    public byte[] getFormCSV(@PathVariable("formId") String formId, HttpServletResponse response) throws URISyntaxException, IOException {
        Optional<User> user = userService.checkAuthoritation();
        Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
        Optional<Form> form=formService.findOne(formId);
        if (user.isPresent() && researcher.isPresent() && form.isPresent() &&
            form.get().getStudy().getResearcher().getId().equals(researcher.get().getId())) {
            response.setContentType("application/zip");
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader("Content-Disposition", "attachment; filename=\""+"Result-Form-"+form.get().getName()+".zip\"");
            return serviceCSV.downloadSingleFormsParticipantsAnswers(form.get());
        }
       return null;
    }

    /**
     * {@code GET /download/study/{studyId}/forms} : get a folder with  some file.csv inside, the file.csv contains all the data of each form of study
     *
     * @param studyId the id of study that with it I want create some file with all  data of each form that belong to it
     * @return array of bytes
     */
    @GetMapping("/download/study/{studyId}/forms")
    public byte[] getFormsCSV(@PathVariable("studyId") String studyId, HttpServletResponse response) throws URISyntaxException, IOException {
        Optional<User> user = userService.checkAuthoritation();
        Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
        Optional<Study> study=studyService.findOne(studyId);
        if (user.isPresent() && researcher.isPresent() && study.isPresent() &&
            study.get().getResearcher().getId().equals(researcher.get().getId())) {
            response.setContentType("application/zip");
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader("Content-Disposition", "attachment; filename=\""+"Result-Forms-Of-"+study.get().getName()+".zip\"");
            return serviceCSV.downloadAllFormsParticipantsAnswers(study.get());

        }
        return  null;
    }

    /**
     * {@code GET /download/study/{studyId}} : get a folder with some file.csv, inside the file.csv contains all the data of one data
     * required of each participantand the have x files like data requested has the study
     *
     * @param studyId the id of study that with it I want create some files with data
     * @return array of bytes
     */
    @GetMapping("/download/study/{studyId}")
    public byte[] getStudyCSV(@PathVariable("studyId") String studyId, HttpServletResponse response) throws URISyntaxException, IOException {
        Optional<User> user = userService.checkAuthoritation();
        Optional<Researcher> researcher = researcherRepository.findOneByUserId(user.get().getId());
        Optional<Study> study=studyService.findOne(studyId);
        if (user.isPresent() && researcher.isPresent() && study.isPresent() &&
            study.get().getResearcher().getId().equals(researcher.get().getId())) {
            response.setContentType("application/zip");
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader("Content-Disposition", "attachment; filename=\""+"Result-Study-"+study.get().getName()+".zip\"");
            return serviceCSV.downloadAllStudyParticipantData(study.get());
        }
        return null;
    }
}
