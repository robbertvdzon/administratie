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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
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
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(request);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                response.status(404);
                return new SingleAnswer("not found");
            }
            //
            String location = "image";          // the directory location where files will be stored
            long maxFileSize = 100000000;       // the maximum size allowed for uploaded files
            long maxRequestSize = 100000000;    // the maximum size allowed for multipart/form-data requests
            int fileSizeThreshold = 1024;       // the size threshold after which files will be written to disk

            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                    location, maxFileSize, maxRequestSize, fileSizeThreshold);
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig",
                    multipartConfigElement);

            Collection<Part> parts = request.raw().getParts();
            for (Part part : parts) {
                System.out.println("Name: " + part.getName());
                System.out.println("Size: " + part.getSize());
                System.out.println("Filename: " + part.getSubmittedFileName());
            }

            String fName = request.raw().getPart("file").getSubmittedFileName();
            System.out.println("Title: " + request.raw().getParameter("title"));
            System.out.println("File: " + fName);

            Part uploadedFile = request.raw().getPart("file");
            Path out = Paths.get(fName);
            out.toFile().delete();
            try (final InputStream in = uploadedFile.getInputStream()) {
                Files.copy(in, out);
                uploadedFile.delete();
            }
            multipartConfigElement = null;
            parts = null;
            uploadedFile = null;

//
            System.out.println("--------");
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
            String bedrag = parts[6];
            String omschrijving = parts[7];

            String uuid=line;
            double bedragAf = getBedragAf(bedrag);
            LocalDate boekDatum = getBoekDatum(date);
            double bedragBij = getBedragBij(bedrag);;
            return new Afschrift(uuid, rekeningNr, rekeningNr, "#1 "+omschrijving, boekDatum, bedragBij, bedragAf);
        }
        else{
            System.out.println(parts.length);
            return null;
        }
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

    private double getBedragBij(String bedrag) {
        double doubleBedrag = new Double(bedrag.replace(",",".")).doubleValue();
        return doubleBedrag>0?doubleBedrag:0;
    }

    private double getBedragAf(String bedrag) {
        double doubleBedrag = new Double(bedrag.replace(",",".")).doubleValue();
        return doubleBedrag<0?doubleBedrag*-1:0;
    }


}
