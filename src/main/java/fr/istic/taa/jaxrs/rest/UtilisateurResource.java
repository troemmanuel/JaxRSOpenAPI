package fr.istic.taa.jaxrs.rest;
import fr.istic.taa.jaxrs.dao.UtilisateurDao;
import fr.istic.taa.jaxrs.domain.Administrateur;
import fr.istic.taa.jaxrs.domain.Utilisateur;
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

    @DELETE
    @Path("/{id}")
    public Response deleteUtilisateur(@PathParam("id") Long id) {
        Utilisateur  utilisateur= utilisateurDao.findOne(id);
        if (utilisateur == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("utilisateur non trouvé").build();
        }
        utilisateurDao.delete( utilisateur);
        return Response.ok().entity("utilisateur supprimé avec succès").build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUtilisateur(@PathParam("id") Long id, Administrateur administrateur) {
        Utilisateur  utilisateur= utilisateurDao.findOne(id);
        if ( utilisateur == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Administrateur non trouvé").build();
        }

        // Mise à jour des champs
        utilisateur.setNom(administrateur.getNom());
        utilisateur.setEmail(administrateur.getEmail());
        utilisateur.setMotDePasse(administrateur.getMotDePasse());

        utilisateurDao.update( utilisateur);
        return Response.ok( utilisateur).build();
    }

    @GET
    public Response findAllAdministrateurs() {
        List< Utilisateur>  utilisateur = utilisateurDao.findAll();
        return Response.ok( utilisateur).build();
    }
}
