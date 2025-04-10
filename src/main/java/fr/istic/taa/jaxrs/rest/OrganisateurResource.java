package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.OrganisateurDao;
import fr.istic.taa.jaxrs.domain.Organisateur;
import fr.istic.taa.jaxrs.domain.Organisateur;
import fr.istic.taa.jaxrs.dto.mapper.ProfilMapper;
import fr.istic.taa.jaxrs.dto.request.ProfilRequestDto;
import fr.istic.taa.jaxrs.dto.response.ProfilResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
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
    public Response getById(@PathParam("id") Long id) {
        Organisateur o = organisateurDao.findOne(id);
        if (o == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(ProfilMapper.toDto(o)).build();
    }

    @GET
    public Response findAll() {
        List<ProfilResponseDto> list = organisateurDao.findAll().stream()
                .map(ProfilMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(list).build();
    }

    @POST
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
    public Response update(@PathParam("id") Long id, ProfilRequestDto dto) {
        Organisateur existing = organisateurDao.findOne(id);
        if (existing == null) return Response.status(Response.Status.NOT_FOUND).build();
        ProfilMapper.updateEntity(existing, dto);
        organisateurDao.update(existing);
        return Response.ok(ProfilMapper.toDto(existing)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Organisateur o = organisateurDao.findOne(id);
        if (o == null) return Response.status(Response.Status.NOT_FOUND).build();
        organisateurDao.delete(o);
        return Response.ok("Supprimé").build();
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
}