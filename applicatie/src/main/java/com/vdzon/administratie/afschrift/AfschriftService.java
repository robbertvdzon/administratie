package com.vdzon.administratie.afschrift;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AfschriftService {

    @Inject
    UserCrud crudService;

    protected Object putAfschrift(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String afschriftJson = req.body();
            Afschrift afschrift = null;
            ObjectMapper mapper = new ObjectMapper();
            AfschriftDto afschriftDto = mapper.readValue(afschriftJson, AfschriftDto.class);
            afschrift = afschriftDto.toAfschrift();

            gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getUuid());
            gebruiker.getDefaultAdministratie().addAfschrift(afschrift);
            crudService.updateGebruiker(gebruiker);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    protected Object removeAfschrift(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String afschriftUuid = req.params(":uuid");
            if ("undefined".equals(afschriftUuid)) {
                afschriftUuid = null;
            }
            gebruiker.getDefaultAdministratie().removeAfschrift(afschriftUuid);
            crudService.updateGebruiker(gebruiker);
            return new SingleAnswer("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    protected Object uploadFile(Request request, Response response) {
        /*
        TODO: Onderstaande code moet heel erg opgeruimd worden!!!
         */
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(request);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                response.status(404);
                return new SingleAnswer("not found");
            }
            String location = "image";          // the directory location where files will be stored
            long maxFileSize = 100000000;       // the maximum size allowed for uploaded files
            long maxRequestSize = 100000000;    // the maximum size allowed for multipart/form-data requests
            int fileSizeThreshold = 1024;       // the size threshold after which files will be written to disk

            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                    location, maxFileSize, maxRequestSize, fileSizeThreshold);
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig",
                    multipartConfigElement);

            String fName = request.raw().getPart("file").getSubmittedFileName();

            Part uploadedFile = request.raw().getPart("file");
            Path out = Paths.get(fName);
            out.toFile().delete();
            try (final InputStream in = uploadedFile.getInputStream()) {
                Files.copy(in, out);
                uploadedFile.delete();
            }
            List<Afschrift> afschriften = parseFile(out);
            System.out.println("count "+afschriften.size());
            for (Afschrift afschrift:afschriften) {
                gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getUuid());
                gebruiker.getDefaultAdministratie().addAfschrift(afschrift);
            }
            crudService.updateGebruiker(gebruiker);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "OK";
    }


    private List<Afschrift> parseFile(Path out) {
        try (Stream<String> stream = Files.lines(out)) {
            return stream.map(line->parseLine(line)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    private Afschrift parseLine(String line){
        //514675950	EUR	20160125	-2,27	-3,02	20160125	-0,75	ABN AMRO Bank N.V.               Betaalpas                   0,75

        String[] parts = line.split("\t");
        if (parts.length==8){
            String rekeningNr = parts[0];
            String date = parts[2];
            String bedragStr = parts[6];
            String omschrijving = parts[7];
            String naam = extractNaam(omschrijving);
            String oms = extractOmschrijving(omschrijving);

            String uuid=line;
            double bedrag = getBedrag(bedragStr);
            LocalDate boekDatum = getBoekDatum(date);
            return new Afschrift(uuid, rekeningNr, oms, naam, boekDatum, bedrag);
        }
        else{
            return null;
        }
    }

    private String extractNaam(String omschrijving) {
//        /TRTP/SEPA OVERBOEKING/IBAN/NL44ABNA0541739336/BIC/ABNANL2A/NAME/RC VAN DER ZON CJ/REMI/overboeking/EREF/NOTPROVIDED
//        SEPA Overboeking                 IBAN: NL82RABO0326286209        BIC: RABONL2U                    Naam: Sibilla                   Omschrijving:
        if (omschrijving.startsWith("/TRTP")){
            String[] split = omschrijving.split("/");
            for (int i = 0; i<split.length; i++){
                if (split[i].equals("NAME")){
                    return split[i+1];
                }
            }
        }
        if (omschrijving.startsWith("SEPA")){
            int pos = omschrijving.indexOf("Naam:");
            int posStart = pos + "Naam:".length();
            if (pos>0) {
                String nextKeyword = findNextKeyword(omschrijving.substring(posStart));
                int posEnd = posStart+omschrijving.substring(posStart).indexOf(nextKeyword);
                if (nextKeyword == null) {
                    return omschrijving.substring(posStart);
                } else {
                    return omschrijving.substring(posStart,posEnd-1);
                }
            }
        }
        return "Onbekend";
    }

    private String extractOmschrijving(String omschrijving) {
//        /TRTP/SEPA OVERBOEKING/IBAN/NL44ABNA0541739336/BIC/ABNANL2A/NAME/RC VAN DER ZON CJ/REMI/overboeking/EREF/NOTPROVIDED
//        SEPA Overboeking                 IBAN: NL82RABO0326286209        BIC: RABONL2U                    Naam: Sibilla                   Omschrijving:
        if (omschrijving.startsWith("/TRTP")){
            String[] split = omschrijving.split("/");
            for (int i = 0; i<split.length; i++){
                if (split[i].equals("REMI")){
                    return split[i+1];
                }
            }
        }
        if (omschrijving.startsWith("SEPA")){
            int pos = omschrijving.indexOf("Omschrijving:");
            int posStart = pos + "Omschrijving:".length();
            if (pos>0) {
                String nextKeyword = findNextKeyword(omschrijving.substring(posStart));
                int posEnd = posStart+omschrijving.substring(posStart).indexOf(nextKeyword);
                if (nextKeyword == null) {
                    return omschrijving.substring(posStart);
                } else {
                    return omschrijving.substring(posStart,posEnd-1);
                }
            }
        }
        return omschrijving;
    }

    private String findNextKeyword(String s){
        String[] words = s.split(" ");
        for (String word:words){
            if (word.endsWith(":")){
                return word;
            }
        }
        return null;
    }

    private LocalDate getBoekDatum(String date) {
        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyymmdd");
            return LocalDate.parse(date, formatter);
        }
        catch (DateTimeParseException exc) {
        }
        return LocalDate.now();

    }

    private double getBedrag(String bedrag) {
        return new Double(bedrag.replace(",",".")).doubleValue();
    }


}
