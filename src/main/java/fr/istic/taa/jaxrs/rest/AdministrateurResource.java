package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.AdministrateurDao;
import fr.istic.taa.jaxrs.domain.Administrateur;
import fr.istic.taa.jaxrs.domain.Pet;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("admin")
@Produces("application/json")  // Ajoute ça ici
public class AdministrateurResource {
    private final AdministrateurDao administrateurDao = new AdministrateurDao();

    @GET
    @Path("/{id}")
    public Administrateur getAdministrateurById(@PathParam("id") Long id) {
        return administrateurDao.findOne(id);
    }

    @POST
    @Consumes("application/json")
    public Response addAdministrateur(@Parameter(description = "Administrateur object", required = true) Administrateur administrateur) {
        administrateurDao.save(administrateur);
        return Response.ok().entity("SUCCESS").build();
    }
}
