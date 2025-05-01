package fr.istic.taa.jaxrs;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Throwable exception) {
        logger.error("Erreur non gérée capturée : {}", exception.getMessage(), exception);
        exception.printStackTrace();

        ErrorMessage message = new ErrorMessage("Une erreur interne est survenue. Veuillez réessayer plus tard.");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(message)
                .build();
    }

    public static class ErrorMessage {
        private String error;

        public ErrorMessage(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
