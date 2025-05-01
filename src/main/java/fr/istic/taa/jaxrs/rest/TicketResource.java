package fr.istic.taa.jaxrs.rest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import fr.istic.taa.jaxrs.dao.EvenementDao;
import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.dao.TicketEtat;
import fr.istic.taa.jaxrs.dao.UtilisateurDao;
import fr.istic.taa.jaxrs.domain.Evenement;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.Utilisateur;
import fr.istic.taa.jaxrs.dto.mapper.TicketMapper;
import fr.istic.taa.jaxrs.dto.request.TicketRequestDto;
import fr.istic.taa.jaxrs.dto.response.TicketResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Obtenir un ticket par ID",
            description = "Retourne les détails d'un ticket spécifique.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Ticket non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
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
    @Operation(
            summary = "Obtenir tous les tickets",
            description = "Retourne une liste de tous les tickets disponibles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des tickets",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class, type = "array")))
            }
    )
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
    @Operation(
            summary = "Acheter un ticket",
            description = "Permet à un utilisateur d'acheter un ticket pour un événement.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket acheté avec succès"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Événement ou utilisateur non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "409", description = "Stock insuffisant pour l'événement",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response acheterTicket(TicketRequestDto dto) {
        System.out.println("achat d'un ticket tickets par l'utilisateur " + dto.getUtilisateurId());
        try {
            System.out.println("Reçu DTO : " + dto);

            // Vérifier si le DTO est null
            if (dto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Le corps de la requête est vide").build();
            }

            // Vérifier si l'ID de l'utilisateur est null
            if (dto.getUtilisateurId() == null) {
                System.out.println("Erreur : utilisateurId est null");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("L'ID de l'utilisateur est requis").build();
            }

            Utilisateur utilisateur = utilisateurDao.findOne(dto.getUtilisateurId());

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
            Ticket ticket = TicketMapper.toEntity(dto, utilisateur, evenement);
            System.out.println("Ticket créé : " + ticket);

            // 2. Mettre l'état du ticket à "PAYE"
            ticket.setEtat(TicketEtat.PAYE.toString());

            // 3. Sauvegarder le ticket pour obtenir l'ID
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

            // Sauvegarder le ticket dans la base de données avec le QR Code et l'état PAYE
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
    @Operation(
            summary = "Annuler un ticket",
            description = "Permet à un utilisateur d'annuler un ticket acheté si l'événement a lieu dans plus de 24h.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket annulé avec succès"),
                    @ApiResponse(responseCode = "400", description = "ID du ticket manquant ou invalide",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Ticket non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Annulation impossible si moins de 24h avant l'événement",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
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

            // 1. Mettre l'état du ticket à "REMBOURSE"
            ticket.setEtat(TicketEtat.REMBOURSE.toString());

            // 2. Sauvegarder le ticket avec l'état mis à jour
            ticketDao.save(ticket);  // Cela met à jour l'état du ticket dans la base de données

            // 3. Ré-augmente le stock de l'événement
            evenement.setStock(evenement.getStock() + 1);
            evenementDao.save(evenement);

            return Response.ok("Ticket annulé et remboursé avec succès").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Erreur lors de l'annulation : " + e.getMessage()).build();
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

    @GET
    @Path("/qrcodes/{ticketId}")
    @Produces("image/png")
    @Operation(
            summary = "Obtenir le code QR d'un ticket",
            description = "Retourne le code QR associé à un ticket.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Code QR retourné",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "image/png")),
                    @ApiResponse(responseCode = "404", description = "Ticket ou code QR non trouvé",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public Response getQRCode(@PathParam("ticketId") Long ticketId) {
        Ticket ticket = ticketDao.findOne(ticketId);
        if (ticket == null || ticket.getCodeQR() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        File qrFile = new File(ticket.getCodeQR());
        if (!qrFile.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(qrFile)
                .header("Content-Disposition", "inline; filename=\"" + qrFile.getName() + "\"")
                .build();
    }

}
