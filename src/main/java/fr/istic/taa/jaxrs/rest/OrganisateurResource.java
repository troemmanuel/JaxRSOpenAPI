package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.OrganisateurDao;
import fr.istic.taa.jaxrs.domain.Organisateur;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("organisateur")
@Produces("application/json")
@Consumes("application/json")
public class OrganisateurResource {
    private final OrganisateurDao organisateurDao = new OrganisateurDao();

    @GET
    @Path("/{id}")
    public Organisateur getOrganisateurById(@PathParam("id") Long id) {
        return organisateurDao.findOne(id);
    }

    @GET
    public List<Organisateur> getAllOrganisateurs() {
        return organisateurDao.findAll();
    }

    @POST
    public Response addOrganisateur(@Parameter(description = "Organisateur object", required = true) Organisateur organisateur) {
        organisateurDao.save(organisateur);
        return Response.ok().entity("Organisateur ajouté avec succès").build();
    }
}
