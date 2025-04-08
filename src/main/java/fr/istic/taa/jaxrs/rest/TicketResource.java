package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.EvenementDao;
import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.dao.UtilisateurDao;
import fr.istic.taa.jaxrs.domain.Evenement;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.Utilisateur;
import fr.istic.taa.jaxrs.dto.mapper.TicketMapper;
import fr.istic.taa.jaxrs.dto.request.TicketRequestDto;
import fr.istic.taa.jaxrs.dto.response.TicketResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("ticket")
@Produces("application/json")
@Consumes("application/json")
public class TicketResource {
    private final TicketDao ticketDao = new TicketDao();
    private final UtilisateurDao utilisateurDao = new UtilisateurDao();
    private final EvenementDao evenementDao = new EvenementDao();


    @GET
    @Path("/{id}")
    public Response getTicketById(@PathParam("id") Long id) {
        Ticket ticket = ticketDao.findOne(id);
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ticket non trouvé pour l'id: " + id).build();
        }
        TicketResponseDto dto = TicketMapper.toDto(ticket);
        return Response.ok(dto).build();
    }

    @GET
    public Response getAllTickets() {
        List<Ticket> tickets = ticketDao.findAll();
        List<TicketResponseDto> dtos = tickets.stream()
                .map(TicketMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTicket(TicketRequestDto dto) {
        try {
            System.out.println("Reçu DTO : " + dto);

            if (dto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Le corps de la requête est vide").build();
            }

            // Vérifie l'ID de l'événement
            if (dto.getEvenementId() == null) {
                System.out.println("Erreur : evenementId est null");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("L'ID de l'événement est requis").build();
            }

            System.out.println("Chargement de l'événement avec ID : " + dto.getEvenementId());
            Evenement evenement = evenementDao.findOne(dto.getEvenementId());

            if (evenement == null) {
                System.out.println("Erreur : Aucun événement trouvé avec l'ID " + dto.getEvenementId());
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Événement non trouvé pour l'ID : " + dto.getEvenementId()).build();
            }

            // utilisateur est null à la création
            Ticket ticket = TicketMapper.toEntity(dto, null, evenement);

            System.out.println("Ticket créé : " + ticket);

            ticketDao.save(ticket);

            return Response.ok("Ticket ajouté avec succès").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Erreur serveur : " + e.getMessage()).build();
        }
    }

}
