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

        return Response.ok().build();
    }
    @DELETE
    @Path("/{id}")
    public Response deleteEvenement(@PathParam("id") Long id) {
        Evenement evenement = evenementDao.findOne(id);
        if (evenement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Événement non trouvé").build();
        }

        evenementDao.delete(evenement);
        return Response.noContent().build(); // ou .ok().build() si tu veux renvoyer 200
    }
    @PUT
    @Path("/{id}")
    public Response updateEvenement(@PathParam("id") Long id, EvenementRequestDto dto) {
        Evenement existing = evenementDao.findOne(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Événement introuvable").build();
        }

        Organisateur organisateur = organisateurDao.findOne(dto.getOrganisateurId());
        if (organisateur == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Organisateur introuvable").build();
        }

        // Mets à jour les champs
        existing.setNom(dto.getNom());
        existing.setLieu(dto.getLieu());
        existing.setDate(dto.getDate());
        existing.setOrganisateur(organisateur);
        // Ajoute ici d'autres champs si nécessaire

        evenementDao.update(existing);

        return Response.ok(EvenementMapper.toDto(existing)).build();
    }

}
