package fr.istic.taa.jaxrs.rest;
import fr.istic.taa.jaxrs.dao.UtilisateurDao;
import fr.istic.taa.jaxrs.domain.Utilisateur;
import fr.istic.taa.jaxrs.domain.Utilisateur;
import fr.istic.taa.jaxrs.domain.Utilisateur;
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

@Path("utilisateur")
@Produces("application/json")
@Consumes("application/json")
public class UtilisateurResource {
    private final UtilisateurDao utilisateurDao = new UtilisateurDao();

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Utilisateur u = utilisateurDao.findOne(id);
        if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(ProfilMapper.toDto(u)).build();
    }

    @GET
    public Response findAll() {
        List<ProfilResponseDto> users = utilisateurDao.findAll().stream()
                .map(ProfilMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(users).build();
    }

    @POST
    public Response create(@Parameter(required = true) ProfilRequestDto dto) {
        Utilisateur user = ProfilMapper.toUtilisateurEntity(dto);
        String email = dto.getEmail();

        // Préparation du filtre avec l'email
        Map<String, Object> filters = new HashMap<>();
        filters.put("email", email);

        if (utilisateurDao.findBy(filters).isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Email déjà utilisé par un utilisateur.").build();
        }
        utilisateurDao.save(user);
        return Response.status(Response.Status.CREATED).entity("Utilisateur ajouté").build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, ProfilRequestDto dto) {
        Utilisateur existing = utilisateurDao.findOne(id);
        if (existing == null) return Response.status(Response.Status.NOT_FOUND).build();
        ProfilMapper.updateEntity(existing, dto);
        utilisateurDao.update(existing);
        return Response.ok(ProfilMapper.toDto(existing)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Utilisateur u = utilisateurDao.findOne(id);
        if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
        utilisateurDao.delete(u);
        return Response.ok("Supprimé").build();
    }
}