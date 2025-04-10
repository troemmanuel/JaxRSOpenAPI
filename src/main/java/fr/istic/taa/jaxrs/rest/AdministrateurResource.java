package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.AdministrateurDao;
import fr.istic.taa.jaxrs.domain.Administrateur;
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

@Path("admin")
@Produces("application/json")
public class AdministrateurResource {
    private final AdministrateurDao administrateurDao = new AdministrateurDao();

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Administrateur admin = administrateurDao.findOne(id);
        if (admin == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(ProfilMapper.toDto(admin)).build();
    }

    @GET
    public Response findAll() {
        List<ProfilResponseDto> admins = administrateurDao.findAll().stream()
                .map(ProfilMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(admins).build();
    }

    @POST
    public Response create(@Parameter(required = true) ProfilRequestDto dto) {
        String email = dto.getEmail();

        // Préparation du filtre avec l'email
        Map<String, Object> filters = new HashMap<>();
        filters.put("email", email);

        // Vérification : email déjà utilisé par un admin
        if (!administrateurDao.findBy(filters).isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Email déjà utilisé par un administrateur.").build();
        }

        Administrateur admin = ProfilMapper.toAdministrateurEntity(dto);
        administrateurDao.save(admin);

        return Response.status(Response.Status.CREATED)
                .entity("SUCCESS").build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, ProfilRequestDto dto) {
        Administrateur existing = administrateurDao.findOne(id);
        if (existing == null) return Response.status(Response.Status.NOT_FOUND).build();
        ProfilMapper.updateEntity(existing, dto);
        administrateurDao.update(existing);
        return Response.ok(ProfilMapper.toDto(existing)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Administrateur admin = administrateurDao.findOne(id);
        if (admin == null) return Response.status(Response.Status.NOT_FOUND).build();
        administrateurDao.delete(admin);
        return Response.ok("Supprimé").build();
    }
}




