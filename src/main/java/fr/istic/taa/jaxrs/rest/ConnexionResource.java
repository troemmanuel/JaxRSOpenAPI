package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.AdministrateurDao;
import fr.istic.taa.jaxrs.dao.OrganisateurDao;
import fr.istic.taa.jaxrs.dao.UtilisateurDao;
import fr.istic.taa.jaxrs.domain.Administrateur;
import fr.istic.taa.jaxrs.domain.Organisateur;
import fr.istic.taa.jaxrs.domain.Utilisateur;
import fr.istic.taa.jaxrs.dto.request.ConnexionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("auth")
@Produces("application/json")
@Consumes("application/json")
public class ConnexionResource {

    private final AdministrateurDao administrateurDao = new AdministrateurDao();
    private final UtilisateurDao utilisateurDao = new UtilisateurDao();
    private final OrganisateurDao organisateurDao = new OrganisateurDao();

    @POST
    @Operation(
            summary = "Authentification d'un utilisateur",
            description = "Permet à un utilisateur (administrateur, utilisateur ou organisateur) de se connecter.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Connexion réussie",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Identifiants incorrects",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erreur interne du serveur",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    public Response connexion(
            @Parameter(description = "Informations de connexion", required = true)
            ConnexionDto credentials) {

        String email = credentials.getEmail();
        String motDePasse = credentials.getMotDePasse();
        Map<String, Object> filters = new HashMap<>();
        filters.put("email", email);

        // 1. Vérifier si c'est un admin
        List<Administrateur> admins = administrateurDao.findBy(filters);
        if (!admins.isEmpty()) {
            Administrateur admin = admins.get(0);
            if (admin != null && admin.getMotDePasse().equals(motDePasse)) {
                return Response.ok()
                        .entity(Map.of(
                                "profil", "admin",
                                "id", admin.getId(),
                                "email", admin.getEmail()
                        )).build();
            }
        }

        // 2. Vérifier si c'est un utilisateur
        List<Utilisateur> utilisateurs = utilisateurDao.findBy(filters);
        if (!utilisateurs.isEmpty()) {
            Utilisateur user = utilisateurs.get(0);
            if (user != null && user.getMotDePasse().equals(motDePasse)) {
                return Response.ok()
                        .entity(Map.of(
                                "profil", "utilisateur",
                                "id", user.getId(),
                                "email", user.getEmail()
                        )).build();
            }
        }

        // 3. Vérifier si c'est un organisateur
        List<Organisateur> organisateurs = organisateurDao.findBy(filters);
        if (!organisateurs.isEmpty()) {
            Organisateur organisateur = organisateurs.get(0);
            if (organisateur != null && organisateur.getMotDePasse().equals(motDePasse)) {
                return Response.ok()
                        .entity(Map.of(
                                "profil", "organisateur",
                                "id", organisateur.getId(),
                                "email", organisateur.getEmail()
                        )).build();
            }
        }

        // 4. Identifiants incorrects
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Email ou mot de passe invalide.").build();
    }
}
