package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.domain.Ticket;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("ticket")
@Produces("application/json")
@Consumes("application/json")
public class TicketResource {
    private final TicketDao ticketDao = new TicketDao();

    @GET
    @Path("/{id}")
    public Ticket getTicketById(@PathParam("id") Long id) {
        return ticketDao.findOne(id);
    }

    @GET
    public List<Ticket> getAllTickets() {
        return ticketDao.findAll();
    }

    @POST
    public Response addTicket(@Parameter(description = "Ticket object", required = true) Ticket ticket) {
        ticketDao.save(ticket);
        return Response.ok().entity("Ticket ajouté avec succès").build();
    }
}
