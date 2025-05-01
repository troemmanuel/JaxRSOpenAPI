package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.UtilisateurDao;
import fr.istic.taa.jaxrs.domain.Utilisateur;
import fr.istic.taa.jaxrs.dto.mapper.ProfilMapper;
import fr.istic.taa.jaxrs.dto.mapper.TicketMapper;
import fr.istic.taa.jaxrs.dto.request.ProfilRequestDto;
import fr.istic.taa.jaxrs.dto.response.ProfilResponseDto;
import fr.istic.taa.jaxrs.dto.response.TicketResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("utilisateur")
@Produces("application/json")
@Consumes("application/json")
public class UtilisateurResource {
    private final UtilisateurDao utilisateurDao = new UtilisateurDao();

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Obtenir un utilisateur par ID",
            description = "Retourne les informations d'un utilisateur spécifique.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfilResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response getById(@PathParam("id") Long id) {
        Utilisateur u = utilisateurDao.findOne(id);
        if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(ProfilMapper.toDto(u)).build();
    }

    @GET
    @Operation(
            summary = "Obtenir tous les utilisateurs",
            description = "Retourne une liste de tous les utilisateurs.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfilResponseDto.class, type = "array")))
            }
    )
    public Response findAll() {
        List<ProfilResponseDto> users = utilisateurDao.findAll().stream()
                .map(ProfilMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(users).build();
    }

    @POST
    @Operation(
            summary = "Créer un utilisateur",
            description = "Permet de créer un nouvel utilisateur.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Utilisateur créé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfilResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Email déjà utilisé par un autre utilisateur",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response create(@Parameter(description = "Détails de l'utilisateur à créer", required = true) ProfilRequestDto dto) {
        Utilisateur user = ProfilMapper.toUtilisateurEntity(dto);
        String email = dto.getEmail();

        Map<String, Object> filters = new HashMap<>();
        filters.put("email", email);

        if (!utilisateurDao.findBy(filters).isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Email déjà utilisé par un utilisateur.").build();
        }

        utilisateurDao.save(user);

        ProfilResponseDto responseDto = ProfilMapper.toDto(user);

        return Response.status(Response.Status.CREATED)
                .entity(responseDto).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Mettre à jour un utilisateur",
            description = "Permet de mettre à jour les informations d'un utilisateur existant.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfilResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response update(@PathParam("id") Long id, ProfilRequestDto dto) {
        Utilisateur existing = utilisateurDao.findOne(id);
        if (existing == null) return Response.status(Response.Status.NOT_FOUND).build();
        ProfilMapper.updateEntity(existing, dto);
        utilisateurDao.update(existing);
        return Response.ok(ProfilMapper.toDto(existing)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Supprimer un utilisateur",
            description = "Permet de supprimer un utilisateur en fonction de son ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response delete(@PathParam("id") Long id) {
        Utilisateur u = utilisateurDao.findOne(id);
        if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
        utilisateurDao.delete(u);
        return Response.ok("Supprimé").build();
    }

    @GET
    @Path("/{id}/tickets")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Obtenir les tickets d'un utilisateur",
            description = "Retourne tous les tickets associés à un utilisateur.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des tickets",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class, type = "array"))),
                    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response getTicketsParUtilisateur(@PathParam("id") Long utilisateurId) {
        Utilisateur utilisateur = utilisateurDao.findOne(utilisateurId);

        if (utilisateur == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Utilisateur non trouvé")
                    .build();
        }

        List<TicketResponseDto> ticketsDto = utilisateur.getTickets().stream()
                .map(TicketMapper::toDto)
                .collect(Collectors.toList());

        return Response.ok(ticketsDto).build();
    }

}