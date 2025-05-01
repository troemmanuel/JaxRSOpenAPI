package fr.istic.taa.jaxrs.rest;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Path("/swagger")
public class SwaggerResource {

    private static final Logger logger = Logger.getLogger(SwaggerResource.class.getName());
    private static final java.nio.file.Path BASE_DIR =  Paths.get("src/main/webapp/swagger").toAbsolutePath().normalize();

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getIndex() {
        return Response
                .seeOther(java.net.URI.create("swagger/index.html"))
                .build();
    }

    @GET
    @Path("{path:.*}")
    public Response getStaticFile(@PathParam("path") String path) {
        return serveFile(path);
    }

    private Response serveFile(String pathStr) {
        try {
            java.nio.file.Path filePath = BASE_DIR.resolve(pathStr).normalize();

            // Prevent directory traversal
            if (!filePath.startsWith(BASE_DIR)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Access denied.")
                        .build();
            }

            if (!Files.exists((java.nio.file.Path) filePath)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("File not found.")
                        .build();
            }

            String mimeType = Files.probeContentType(filePath);
            byte[] fileData = Files.readAllBytes(filePath);

            return Response.ok(fileData)
                    .type(mimeType != null ? mimeType : MediaType.APPLICATION_OCTET_STREAM)
                    .build();

        } catch (IOException e) {
            logger.severe("Error reading file: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error reading file.")
                    .build();
        }
    }
}
