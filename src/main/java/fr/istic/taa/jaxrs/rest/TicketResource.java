package fr.istic.taa.jaxrs.rest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
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
    public Response acheterTicket(TicketRequestDto dto) {
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

            // Vérifie le stock disponible
            if (evenement.getStock() <= 0) {
                System.out.println("Erreur : Stock insuffisant pour l'événement");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Plus de places disponibles pour cet événement").build();
            }

            // 1. Créer le ticket (sans QR code)
            Ticket ticket = TicketMapper.toEntity(dto, null, evenement);
            System.out.println("Ticket créé : " + ticket);

            // 2. Sauvegarder le ticket pour obtenir l'ID
            ticketDao.save(ticket); // Cela génère l'ID

            // Générer le code QR pour le ticket
            String qrCodeData = "tremaXyama" + UUID.randomUUID();
            String fileName = "qr_code_" + ticket.getId() + ".png";

            String relativePath = "src/main/java/fr/istic/taa/jaxrs/upload/qrcodes/" + fileName;
            String absolutePath = new File(relativePath).getAbsolutePath();

            File directory = new File("src/main/java/fr/istic/taa/jaxrs/upload/qrcodes/");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            generateQRCode(qrCodeData, absolutePath);
            ticket.setCodeQR(relativePath);

            // Diminuer le stock de l'événement
            evenement.setStock(evenement.getStock() - 1);
            evenementDao.save(evenement); // Sauvegarder le nouvel état de l'événement

            // Sauvegarder le ticket dans la base de données avec le QR Code
            ticketDao.save(ticket);

            return Response.ok("Ticket acheté avec succès avec QR Code").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Erreur serveur : " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/annuler/{id}")
    public Response annulerTicket(@PathParam("id") Long ticketId) {
        try {
            if (ticketId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("L'ID du ticket est requis").build();
            }

            Ticket ticket = ticketDao.findOne(ticketId);
            if (ticket == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Aucun ticket trouvé avec l'ID : " + ticketId).build();
            }

            Evenement evenement = ticket.getEvenement();
            if (evenement == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Événement associé introuvable").build();
            }

            // Vérifie si l'annulation est possible (24h avant l'événement)
            Date now = new Date();
            long timeDiff = evenement.getDate().getTime() - now.getTime();
            long hoursDiff = timeDiff / (1000 * 60 * 60);

            if (hoursDiff < 24) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Annulation impossible : moins de 24h avant l'événement").build();
            }

            // Supprime le ticket
            ticketDao.delete(ticket);

            // Ré-augmente le stock
            evenement.setStock(evenement.getStock() + 1);
            evenementDao.save(evenement);

            return Response.ok("Ticket annulé avec succès").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Erreur lors de l'annulation : " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/utilisateurs/{id}/tickets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketsParUtilisateur(@PathParam("id") Long utilisateurId) {
        try {
            if (utilisateurId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("L'ID de l'utilisateur est requis").build();
            }

            Map<String, Object> filters = new HashMap<>();
            filters.put("utilisateur.id", utilisateurId); // clé conforme au nom du champ dans l'entité Ticket

            List<Ticket> tickets = ticketDao.findBy(filters);

            if (tickets == null || tickets.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Aucun ticket trouvé pour cet utilisateur").build();
            }

            return Response.ok(tickets).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Erreur serveur : " + e.getMessage()).build();
        }
    }




    private void generateQRCode(String data, String filePath) throws Exception {
        // Configurer les paramètres pour le code QR
        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hintMap.put(EncodeHintType.MARGIN, 1); // Marges du code QR

        // Créer le code QR
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 300, 300, hintMap);

        // Convertir le matrix en image
        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                image.setRGB(i, j, matrix.get(i, j) ? 0x000000 : 0xFFFFFF); // Noir et blanc
            }
        }

        // Sauvegarder l'image du code QR sur le serveur
        File file = new File(filePath);
        ImageIO.write(image, "PNG", file);
    }

}
