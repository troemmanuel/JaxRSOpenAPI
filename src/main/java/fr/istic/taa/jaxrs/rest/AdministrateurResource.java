package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.AdministrateurDao;
import fr.istic.taa.jaxrs.domain.Administrateur;
import fr.istic.taa.jaxrs.dto.mapper.ProfilMapper;
import fr.istic.taa.jaxrs.dto.request.ProfilRequestDto;
import fr.istic.taa.jaxrs.dto.response.ProfilResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("admin")
@Produces("application/json")
public class AdministrateurResource {

    private final AdministrateurDao administrateurDao = new AdministrateurDao();

    @GET
    @Path("/{id}")
    @Operation(summary = "Récupérer un administrateur par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrateur trouvé",
                    content = @Content(schema = @Schema(implementation = ProfilResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    public Response getById(
            @Parameter(description = "ID de l'administrateur", required = true)
            @PathParam("id") Long id) {

        Administrateur admin = administrateurDao.findOne(id);
        if (admin == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(ProfilMapper.toDto(admin)).build();
    }

    @GET
    @Operation(summary = "Lister tous les administrateurs")
    @ApiResponse(responseCode = "200", description = "Liste des administrateurs",
            content = @Content(schema = @Schema(implementation = ProfilResponseDto.class)))
    public Response findAll() {
        List<ProfilResponseDto> admins = administrateurDao.findAll().stream()
                .map(ProfilMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(admins).build();
    }

    @POST
    @Operation(summary = "Créer un nouvel administrateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrateur créé",
                    content = @Content(schema = @Schema(implementation = ProfilResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Email déjà utilisé")
    })
    public Response create(
            @RequestBody(description = "Données du nouvel administrateur", required = true,
                    content = @Content(schema = @Schema(implementation = ProfilRequestDto.class)))
            ProfilRequestDto dto) {

        String email = dto.getEmail();
        Map<String, Object> filters = new HashMap<>();
        filters.put("email", email);

        if (!administrateurDao.findBy(filters).isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Email déjà utilisé par un administrateur.").build();
        }

        Administrateur admin = ProfilMapper.toAdministrateurEntity(dto);
        administrateurDao.save(admin);

        return Response.status(Response.Status.CREATED)
                .entity(ProfilMapper.toDto(admin)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Mettre à jour un administrateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrateur mis à jour",
                    content = @Content(schema = @Schema(implementation = ProfilResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    public Response update(
            @Parameter(description = "ID de l'administrateur", required = true)
            @PathParam("id") Long id,
            @RequestBody(description = "Nouvelles données administrateur", required = true,
                    content = @Content(schema = @Schema(implementation = ProfilRequestDto.class)))
            ProfilRequestDto dto) {

        Administrateur existing = administrateurDao.findOne(id);
        if (existing == null) return Response.status(Response.Status.NOT_FOUND).build();

        ProfilMapper.updateEntity(existing, dto);
        administrateurDao.update(existing);
        return Response.ok(ProfilMapper.toDto(existing)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Supprimer un administrateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrateur supprimé"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    public Response delete(
            @Parameter(description = "ID de l'administrateur", required = true)
            @PathParam("id") Long id) {

        Administrateur admin = administrateurDao.findOne(id);
        if (admin == null) return Response.status(Response.Status.NOT_FOUND).build();

        administrateurDao.delete(admin);
        return Response.ok("Supprimé").build();
    }
}
