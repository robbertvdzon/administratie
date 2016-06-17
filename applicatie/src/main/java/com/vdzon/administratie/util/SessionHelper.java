package com.vdzon.administratie.util;

import com.vdzon.administratie.exceptions.ForbiddenException;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.Gebruiker;
import spark.Request;
import spark.Session;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SessionHelper {

    private static final String AUTHENTICATED_USER_UUID = "authenticatedUserUuid";

    public static String getAuthenticatedUserUuid(Request req) {
        Session session = req.session(true);
        String uuid = session.<String>attribute(AUTHENTICATED_USER_UUID);
        return uuid;
    }

    public static void setAuthenticatedUserUuid(Request req, String uuid) {
        Session session = req.session(true);
        session.attribute(AUTHENTICATED_USER_UUID, uuid);
    }

    public static void removeAuthenticatedUserUuid(Request req) {
        Session session = req.session(true);
        session.removeAttribute(AUTHENTICATED_USER_UUID);
    }

    public static Gebruiker getGebruikerOrThowForbiddenExceptin(Request req, UserCrud crudService) {
        String uuid = getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null) {
            throw new ForbiddenException();
        }
        return gebruiker;
    }

    public static Path getUploadedFile(Request request) {
        try {
            String location = "image";
            long maxFileSize = 100000000;
            long maxRequestSize = 100000000;
            int fileSizeThreshold = 1024;

            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                    location, maxFileSize, maxRequestSize, fileSizeThreshold);
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            String filename = request.raw().getPart("file").getSubmittedFileName();
            Part uploadedFile = request.raw().getPart("file");
            Path out = Paths.get(filename);
            out.toFile().delete();
            try (final InputStream in = uploadedFile.getInputStream()) {
                Files.copy(in, out);
                uploadedFile.delete();
            }
            return out;
        } catch (IOException|ServletException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
