package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.OrganisateurDao;
import fr.istic.taa.jaxrs.domain.Organisateur;
import fr.istic.taa.jaxrs.dto.mapper.ProfilMapper;
import fr.istic.taa.jaxrs.dto.request.ProfilRequestDto;
import fr.istic.taa.jaxrs.dto.response.EvenementResponseDto;
import fr.istic.taa.jaxrs.dto.response.ProfilResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("organisateur")
@Produces("application/json")
@Consumes("application/json")
public class OrganisateurResource {
    private final OrganisateurDao organisateurDao = new OrganisateurDao();

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Récupérer un organisateur par ID",
            description = "Retourne les détails d'un organisateur spécifique.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Organisateur trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfilResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Organisateur non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response getById(@PathParam("id") Long id) {
        Organisateur o = organisateurDao.findOne(id);
        if (o == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(ProfilMapper.toDto(o)).build();
    }

    @GET
    @Operation(
            summary = "Récupérer tous les organisateurs",
            description = "Retourne une liste de tous les organisateurs.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des organisateurs",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfilResponseDto.class, type = "array")))
            }
    )
    public Response findAll() {
        List<ProfilResponseDto> list = organisateurDao.findAll().stream()
                .map(ProfilMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(list).build();
    }

    @POST
    @Operation(
            summary = "Ajouter un nouvel organisateur",
            description = "Ajoute un nouvel organisateur au système.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Organisateur ajouté avec succès",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfilResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Email déjà utilisé par un organisateur",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response create(@Parameter(required = true) ProfilRequestDto dto) {
        Organisateur o = ProfilMapper.toOrganisateurEntity(dto);
        String email = dto.getEmail();

        // Préparation du filtre avec l'email
        Map<String, Object> filters = new HashMap<>();
        filters.put("email", email);

        // Vérification si l'email est déjà utilisé par un organisateur
        if (!organisateurDao.findBy(filters).isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Email déjà utilisé par un organisateur.").build();
        }

        // Sauvegarde de l'organisateur
        organisateurDao.save(o);

        // Mapping vers le DTO de réponse
        ProfilResponseDto responseDto = ProfilMapper.toDto(o);

        // Renvoi de la réponse avec le DTO
        return Response.status(Response.Status.CREATED)
                .entity(responseDto).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Mettre à jour un organisateur",
            description = "Met à jour les informations d'un organisateur existant.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Organisateur mis à jour",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfilResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Organisateur non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response update(@PathParam("id") Long id, ProfilRequestDto dto) {
        Organisateur existing = organisateurDao.findOne(id);
        if (existing == null) return Response.status(Response.Status.NOT_FOUND).build();
        ProfilMapper.updateEntity(existing, dto);
        organisateurDao.update(existing);
        return Response.ok(ProfilMapper.toDto(existing)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Supprimer un organisateur",
            description = "Supprime un organisateur du système.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Organisateur supprimé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Organisateur non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response delete(@PathParam("id") Long id) {
        Organisateur o = organisateurDao.findOne(id);
        if (o == null) return Response.status(Response.Status.NOT_FOUND).build();
        organisateurDao.delete(o);
        return Response.ok("Supprimé").build();
    }

    @GET
    @Path("/{id}/evenements")
    @Operation(
            summary = "Récupérer les événements par organisateur",
            description = "Retourne les événements associés à un organisateur.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des événements",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EvenementResponseDto.class, type = "array"))),
                    @ApiResponse(responseCode = "404", description = "Organisateur non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response getEvenementsByOrganisateur(@PathParam("id") Long id) {
        Organisateur organisateur = organisateurDao.findOne(id);
        if (organisateur == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Organisateur non trouvé").build();
        }
        return Response.ok(organisateur.getEvenements()).build();
    }
}
