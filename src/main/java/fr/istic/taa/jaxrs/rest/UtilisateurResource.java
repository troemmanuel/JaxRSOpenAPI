package fr.istic.taa.jaxrs.rest;
import fr.istic.taa.jaxrs.dao.UtilisateurDao;
import fr.istic.taa.jaxrs.domain.Utilisateur;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("utilisateur")
@Produces("application/json")
@Consumes("application/json")
public class UtilisateurResource {
    private final UtilisateurDao utilisateurDao = new UtilisateurDao();

    @GET
    @Path("/{id}")
    public Utilisateur getUtilisateurById(@PathParam("id") Long id) {
        return utilisateurDao.findOne(id);
    }

    @GET
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurDao.findAll();
    }

    @POST
    public Response addUtilisateur(@Parameter(description = "Utilisateur object", required = true) Utilisateur utilisateur) {
        utilisateurDao.save(utilisateur);
        return Response.ok().entity("Utilisateur ajouté avec succès").build();
    }
}
