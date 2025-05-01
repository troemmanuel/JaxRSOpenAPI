package fr.istic.taa.jaxrs.dto.response;

import fr.istic.taa.jaxrs.dto.TicketShortDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class EvenementResponseDto {

    @Schema(description = "Identifiant unique de l'événement", example = "1", required = true)
    private Long id;

    @Schema(description = "Nom de l'événement", example = "Concert de Rock", required = true)
    private String nom;

    @Schema(description = "Date de l'événement", example = "2025-12-25T20:00:00Z", required = true)
    private Date date;

    @Schema(description = "Lieu de l'événement", example = "Stade Pierre-Mauroy, Lille", required = true)
    private String lieu;

    @Schema(description = "Capacité maximale de l'événement", example = "50000", required = true)
    private int capacite;

    @Schema(description = "Description de l'événement", example = "Un concert de rock avec plusieurs artistes", required = true)
    private String description;

    @Schema(description = "État de l'événement (ex: ouvert, fermé)", example = "ouvert", required = true)
    private String etat;

    @Schema(description = "Nombre de tickets restants", example = "500", required = true)
    private int stock;

    @Schema(description = "Genre de l'événement", example = "Musique", required = true)
    private String genre;

    @Schema(description = "Artiste principal de l'événement", example = "The Rolling Stones", required = true)
    private String artiste;

    @Schema(description = "Identifiant de l'organisateur de l'événement", example = "2", required = true)
    private Long organisateurId;

    @Schema(description = "Prix du ticket pour l'événement", example = "50.00", required = true)
    private double prix;

    @Schema(description = "Liste des tickets associés à l'événement", required = true)
    private List<TicketShortDto> tickets;

    // Getters & Setters

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
}
