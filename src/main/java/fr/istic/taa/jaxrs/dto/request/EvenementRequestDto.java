package fr.istic.taa.jaxrs.dto.request;

import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.dto.TicketShortDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class EvenementRequestDto {

    @Schema(description = "Identifiant de l'événement", example = "1")
    private Long id;

    @Schema(description = "Nom de l'événement", example = "Concert de Rock", required = true)
    private String nom;

    @Schema(description = "Date de l'événement", example = "2025-06-15T20:00:00", required = true)
    private Date date;

    @Schema(description = "Lieu de l'événement", example = "Stade de France", required = true)
    private String lieu;

    @Schema(description = "Capacité maximale de l'événement", example = "10000", required = true)
    private int capacite;

    @Schema(description = "Description détaillée de l'événement", example = "Un concert exceptionnel avec des artistes internationaux", required = true)
    private String description;

    @Schema(description = "État actuel de l'événement (ex: ouvert, fermé, annulé)", example = "ouvert")
    private String etat;

    @Schema(description = "Stock actuel de billets disponibles", example = "200", required = true)
    private int stock;

    @Schema(description = "Genre de l'événement (ex: concert, théâtre, conférence)", example = "concert")
    private String genre;

    @Schema(description = "Artiste principal de l'événement", example = "The Rolling Stones")
    private String artiste;

    @Schema(description = "Identifiant de l'organisateur de l'événement", example = "3", required = true)
    private Long organisateurId;

    @Schema(description = "Prix du billet pour l'événement", example = "50.0", required = true)
    private double prix;

    @Schema(description = "Liste des tickets associés à cet événement")
    private List<TicketShortDto> tickets;

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public Long getOrganisateurId() {
        return organisateurId;
    }

    public void setOrganisateurId(Long organisateurId) {
        this.organisateurId = organisateurId;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public List<TicketShortDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketShortDto> tickets) {
        this.tickets = tickets;
    }

    public static TicketShortDto toTicketShortDto(Ticket ticket) {
        TicketShortDto dto = new TicketShortDto();
        dto.setId(ticket.getId());
        dto.setCodeQR(ticket.getCodeQR());
        dto.setEtat(ticket.getEtat());
        dto.setPrix(ticket.getPrix());
        return dto;
    }
}
