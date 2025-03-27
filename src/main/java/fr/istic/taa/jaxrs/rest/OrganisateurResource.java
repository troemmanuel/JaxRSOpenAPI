package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.OrganisateurDao;
import fr.istic.taa.jaxrs.domain.Organisateur;
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
    @GET
    @Path("/{id}/evenements")
    public Response getEvenementsByOrganisateur(@PathParam("id") Long id) {
        Organisateur organisateur = organisateurDao.findOne(id);
        if (organisateur == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Organisateur non trouvé").build();
        }

        return Response.ok(organisateur.getEvenements()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrganisateur(@PathParam("id") Long id) {
        Organisateur organisateur= organisateurDao.findOne(id);
        if (organisateur == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("organisateur non trouvé").build();
        }
        organisateurDao.delete(organisateur);
        return Response.ok().entity("organisateur supprimé avec succès").build();
    }

    @PUT
    @Path("/{id}")
    public Response updateOrganisateur(@PathParam("id") Long id, Organisateur organisateur) {
        Organisateur existingAdmin = organisateurDao.findOne(id);
        if (existingAdmin == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Organisateur non trouvé").build();
        }

        // Mise à jour des champs
        existingAdmin.setNom(organisateur.getNom());
        existingAdmin.setEmail(organisateur.getEmail());
        existingAdmin.setMotDePasse(organisateur.getMotDePasse());

        organisateurDao.update(existingAdmin);
        return Response.ok(existingAdmin).build();
    }

    @GET
    public Response findAllOrganisateurs() {
        List<Organisateur> admins =organisateurDao.findAll();
        return Response.ok(admins).build();
    }
}
