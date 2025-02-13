package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.EvenementDao;
import fr.istic.taa.jaxrs.domain.Evenement;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("evenement")
@Produces("application/json")
@Consumes("application/json")
public class EvenementResource {
    private final EvenementDao evenementDao = new EvenementDao();

    @GET
    @Path("/{id}")
    public Evenement getEvenementById(@PathParam("id") Long id) {
        return evenementDao.findOne(id);
    }

    @GET
    public List<Evenement> getAllEvenements() {
        return evenementDao.findAll();
    }

    @POST
    public Response addEvenement(@Parameter(description = "Evenement object", required = true) Evenement evenement) {
        evenementDao.save(evenement);
        return Response.ok().entity("Événement ajouté avec succès").build();
    }
}
