package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.EvenementDao;
import fr.istic.taa.jaxrs.dao.OrganisateurDao;
import fr.istic.taa.jaxrs.domain.Evenement;
import fr.istic.taa.jaxrs.domain.Organisateur;
import fr.istic.taa.jaxrs.dto.mapper.EvenementMapper;
import fr.istic.taa.jaxrs.dto.request.EvenementRequestDto;
import fr.istic.taa.jaxrs.dto.response.EvenementResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("evenement")
@Produces("application/json")
@Consumes("application/json")
public class EvenementResource {

    private final EvenementDao evenementDao = new EvenementDao();
    private final OrganisateurDao organisateurDao = new OrganisateurDao();

    @GET
    @Path("/{id}")
    public Response getEvenementById(@PathParam("id") Long id) {
        Evenement evenement = evenementDao.findOne(id);
        if (evenement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Événement non trouvé").build();
        }

        EvenementResponseDto dto = EvenementMapper.toDto(evenement);
        return Response.ok(dto).build();
    }

    @GET
    public Response getAllEvenements() {
        List<Evenement> evenements = evenementDao.findAll();
        List<EvenementResponseDto> dtos = evenements.stream()
                .map(EvenementMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @POST
    public Response addEvenement(@Parameter(description = "Evenement object", required = true) EvenementRequestDto dto) {
        Organisateur organisateur = organisateurDao.findOne(dto.getOrganisateurId());

        if (organisateur == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Organisateur introuvable").build();
        }

        Evenement evenement = EvenementMapper.toEntity(dto, organisateur);
        evenementDao.save(evenement);

        return Response.status(Response.Status.CREATED)
                .entity("Événement ajouté avec succès")
                .build();
    }
}
