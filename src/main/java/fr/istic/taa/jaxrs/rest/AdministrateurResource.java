package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.AdministrateurDao;
import fr.istic.taa.jaxrs.domain.Administrateur;
import fr.istic.taa.jaxrs.domain.Pet;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("admin")
@Produces("application/json")
public class AdministrateurResource { private final AdministrateurDao administrateurDao = new AdministrateurDao();

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



    @DELETE
    @Path("/{id}")
    public Response deleteAdministrateur(@PathParam("id") Long id) {
        Administrateur admin = administrateurDao.findOne(id);
        if (admin == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Administrateur non trouvé").build();
        }
        administrateurDao.delete(admin);
        return Response.ok().entity("Administrateur supprimé avec succès").build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAdministrateur(@PathParam("id") Long id, Administrateur administrateur) {
        Administrateur existingAdmin = administrateurDao.findOne(id);
        if (existingAdmin == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Administrateur non trouvé").build();
        }

        // Mise à jour des champs
        existingAdmin.setNom(administrateur.getNom());
        existingAdmin.setEmail(administrateur.getEmail());
        existingAdmin.setMotDePasse(administrateur.getMotDePasse());

        administrateurDao.update(existingAdmin);
        return Response.ok(existingAdmin).build();
    }

    @GET
    public Response findAllAdministrateurs() {
        List<Administrateur> admins = administrateurDao.findAll();
        return Response.ok(admins).build();
    }
}




