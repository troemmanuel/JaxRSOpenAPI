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
    public Response addTicket(TicketRequestDto ticketDto) {
        Utilisateur utilisateur = utilisateurDao.findOne(ticketDto.getUtilisateurId());
        Evenement evenement = evenementDao.findOne(ticketDto.getEvenementId());

        if (utilisateur == null || evenement == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Utilisateur ou Événement introuvable").build();
        }

        Ticket ticket = TicketMapper.toEntity(ticketDto, utilisateur, evenement);
        ticketDao.save(ticket);

        TicketResponseDto responseDto = TicketMapper.toDto(ticket);
        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }
}
