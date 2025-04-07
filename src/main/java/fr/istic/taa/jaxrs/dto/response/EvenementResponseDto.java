package fr.istic.taa.jaxrs.dto.response;

import fr.istic.taa.jaxrs.dto.TicketShortDto;

import java.util.Date;
import java.util.List;

public class EvenementResponseDto {
    private Long id;
    private String nom;
    private Date date;
    private String lieu;
    private int capacite;
    private String description;
    private String etat;
    private int stock;
    private String genre;
    private String artiste;
    private Long organisateurId;
    private List<TicketShortDto> tickets;

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

    public List<TicketShortDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketShortDto> tickets) {
        this.tickets = tickets;
    }
}
