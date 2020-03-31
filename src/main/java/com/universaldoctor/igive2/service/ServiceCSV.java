package com.universaldoctor.igive2.service;

import com.universaldoctor.igive2.domain.Data;
import com.universaldoctor.igive2.domain.Form;
import com.universaldoctor.igive2.domain.Participant;
import com.universaldoctor.igive2.domain.Study;
import com.universaldoctor.igive2.domain.enumeration.DataType;
import com.universaldoctor.igive2.service.dto.FormResult;
import com.universaldoctor.igive2.service.dto.ParticipantData;
import com.universaldoctor.igive2.service.dto.QuestionsAnswers;
import org.apache.commons.io.IOUtils;


import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



public class ServiceCSV {

    private final FormService formService;
    private final ParticipantService participantService;
    private final DataService dataService;

    public ServiceCSV(FormService formService,DataService dataService,ParticipantService participantService) {
        this.formService = formService;
        this.dataService=dataService;
        this.participantService=participantService;
    }

    public byte[] downloadAllStudyParticipantData(Study study) throws IOException {
        Map<String, String> map = new HashMap<>();
        Optional<Set<Participant>> participants=participantService.getFromStudy(study);
        if(participants.isPresent() && participants.get()!=null && !participants.get().isEmpty()){
            String requestedData= study.getRequestedData();
            if (requestedData.contains("BLOODPRESSURE")){
                String s = createDataTypeCSV(participants.get(),DataType.SYSTOLIC);
                map.put("Result-Study-BLOODPRESSURE",s);
            }
            if(requestedData.contains("WEIGHT")){
                String s = createDataTypeCSV(participants.get(),DataType.WEIGHT);
                map.put("Result-Study-WEIGHT",s);
            }
            if(requestedData.contains("HEIGHT")){
                String s = createDataTypeCSV(participants.get(),DataType.HEIGHT);
                map.put("Result-Study-HEIGHT",s);
            }
            if(requestedData.contains("STEPS")){
                String s = createDataTypeCSV(participants.get(),DataType.STEPS);
                map.put("Result-Study-STEPS",s);
            }
            if(requestedData.contains("ACTIVETIME")){
                String s = createDataTypeCSV(participants.get(),DataType.ACTIVETIME);
                map.put("Result-Study-ACTIVETIME",s);
            }
            if(requestedData.contains("SEATEDTIME")){
                String s = createDataTypeCSV(participants.get(),DataType.SEATEDTIME);
                map.put("Result-Study-SEATEDTIME",s);
            }
            if(requestedData.contains("BREATHINGPATTERN")){
                String s = createDataTypeCSV(participants.get(),DataType.BREATHINGPATTERN);
                map.put("Result-Study-BREATHINGPATTERN",s);
            }
            if(requestedData.contains("BREATHINGRAWDATA")){
                String s = createDataTypeCSV(participants.get(),DataType.BREATHINGRAWDATA);
                map.put("Result-Study-BREATHINGRAWDATA",s);
            }
            if(requestedData.contains("SLEEP")){
                String s = createDataTypeCSV(participants.get(),DataType.SLEEP);
                map.put("Result-Study-SLEEP",s);
            }
            if(requestedData.contains("HEARTHRATE")){
                String s = createDataTypeCSV(participants.get(),DataType.HEARTHRATE);
                map.put("Result-Study-HEARTHRATE",s);
            }
            return makeZip(map);
        }
        return null;
    }

    /*crea una carpeta i dentro  esta los archivos csv de cada form del estudio, devuelve la path de la carpeta*/
    public byte[] downloadAllFormsParticipantsAnswers(Study study) throws IOException {//falta comprimir
        Map<String, String> map = new HashMap<>();
        Optional<Set<Form>> forms=formService.getForms(study);
        if(forms.isPresent() && forms.get()!=null && !forms.get().isEmpty()){
            Iterator<Form> iterator= forms.get().iterator();
            while (iterator.hasNext()){
                FormResult singleForms = formService.getFormResult(iterator.next());
                String s=createCSVFromForm(singleForms);
                map.put("Result-Form-"+singleForms.getFormName(),s);
            }
            return makeZip(map);
        }
        return null;
    }

    /*crea una carpeta i dentro esta el archivo csv del form, devuelve la path de la carpeta*/
    public byte[] downloadSingleFormsParticipantsAnswers(Form form) throws IOException {//falta comprimir
        Map<String, String> map = new HashMap<>();
        FormResult singleForms = formService.getFormResult(form);
        /*File carpeta=new File("Result-Form-"+form.getName());
        carpeta.mkdirs();*/
        String s=createCSVFromForm(singleForms);
        map.put("Result-Form-"+singleForms.getFormName(),s);
        return makeZip( map);
    }

    private String createCSVFromForm(FormResult singleForms) throws IOException {
        StringBuffer result = new StringBuffer();
        ArrayList<ParticipantData> participantData=singleForms.getParticipantResponses();
        if(!participantData.isEmpty()) {
            result.append("ParticipantID");
            result.append(";");
            result.append("Question");
            result.append(";");
            result.append("Answer");
            //campos
            result.append("\n");// salto de linea

            //recorremos la lista e insertamos
            for (ParticipantData data : participantData) {
                ArrayList<QuestionsAnswers> questionsAnswers=data.getResponses();
                if(!questionsAnswers.isEmpty()) {
                    for (QuestionsAnswers answers : questionsAnswers) {
                        result.append(data.getAnonymousId());
                        result.append(";");
                        result.append(answers.getQuestions());
                        result.append(";");
                        result.append(answers.getAnswer());
                        result.append("\n");
                    }
                }
                result.append("\n");
            }
        }
        return result.toString();
    }

    /*crea un archivo de cada dato requerido del estudio correspondiente*/
    public String createDataTypeCSV( Set<Participant> participants, DataType dataType) throws IOException {
        StringBuffer result = new StringBuffer();
        if(dataType==DataType.SYSTOLIC || dataType==DataType.DYASTOLIC){
            result.append("ParticipantID");
            result.append(";");
            result.append("SYSTOLIC");
            result.append(";");
            result.append("Date systolic");
            result.append(";");
            result.append("DYASTOLIC");
            result.append(";");
            result.append("Date dyastolic");
            result.append("\n");// salto de linea
        }else{
            result.append("ParticipantID");
            result.append(";");
            result.append(dataType.toString());
            result.append(";");
            result.append("Date");
            result.append("\n");
        }

        for(Participant participant:participants){
            if(dataType==DataType.SYSTOLIC || dataType==DataType.DYASTOLIC){
                Optional<Set<Data>> systolic=dataService.getDataOfTypeDataOrderByDate(participant.getMobileUser(),DataType.SYSTOLIC);
                Optional<Set<Data>> dyastolic=dataService.getDataOfTypeDataOrderByDate(participant.getMobileUser(),DataType.DYASTOLIC);
                if(systolic.isPresent() && dyastolic.isPresent() && systolic.get()!=null && dyastolic.get()!=null &&
                    !systolic.get().isEmpty() && !dyastolic.get().isEmpty()){
                    Iterator<Data> sys=systolic.get().iterator();
                    Iterator<Data> dys=dyastolic.get().iterator();
                    while(sys.hasNext() && dys.hasNext()){
                        Data systol= sys.next();
                        Data dyastol= dys.next();
                        result.append(participant.getAnonymousId());
                        result.append(";");
                        result.append(systol.getValue());
                        result.append(";");
                        result.append(systol.getDate().toString());
                        result.append(";");
                        result.append(dyastol.getValue());
                        result.append(";");
                        result.append(dyastol.getDate().toString());
                        result.append("\n");
                    }
                }
            }else{
                Optional<Set<Data>> data=dataService.getDataOfTypeDataOrderByDate(participant.getMobileUser(),dataType);
                if(data.isPresent() && data.get()!= null && !data.get().isEmpty()){
                    for(Data dat: data.get()){
                        result.append(participant.getAnonymousId());
                        result.append(";");
                        result.append(dat.getValue());
                        result.append(";");
                        result.append(dat.getDate().toString());
                        result.append("\n");
                    }
                }
            }
        }
        return result.toString();
    }

    //creo el zip
  /*  private byte[]  makeZip(String fileName)throws IOException, FileNotFoundException{
        File file = new File(fileName);
        // zos = new ZipOutputStream(new FileOutputStream(file + ".zip"));//del original
        ZipOutputStream zipOutputStream= new ZipOutputStream(bufferedOutputStream);
        //recurseFiles(files,zipOutputStream);
        //zos.close();//del original
        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }*/

    private byte[]  makeZip( Map<String, String> files)throws IOException, FileNotFoundException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream  = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream= new ZipOutputStream(bufferedOutputStream);
        recurseFiles(files,zipOutputStream);
        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    // añado todos los files dentro del zip
    private   void recurseFiles(Map<String, String> files,ZipOutputStream zipOutputStream)throws IOException, FileNotFoundException {
        Iterator<String> it = files.keySet().iterator();
        while (it.hasNext()){
            String name = it.next();
            byte[] buf = new byte[1024];
            int len;
            ByteArrayInputStream fin = new ByteArrayInputStream(files.get(name).getBytes());
            BufferedInputStream in = new BufferedInputStream(fin);
            zipOutputStream.putNextEntry(new ZipEntry(name+".csv"));
            while ((len = in.read(buf)) >= 0) {
                zipOutputStream.write(buf, 0, len);
            }
            in.close();
            fin.close();
        }

        zipOutputStream.closeEntry();

    }

}
