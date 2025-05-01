package fr.istic.taa.jaxrs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class TicketResponseDto {

    @Schema(description = "Identifiant unique du ticket", example = "1", required = true)
    private Long id;

    @Schema(description = "Code QR associé au ticket", example = "abc123xyz", required = true)
    private String codeQR;

    @Schema(description = "État actuel du ticket (ex: utilisé, non utilisé)", example = "non utilisé", required = true)
    private String etat;

    @Schema(description = "Prix du ticket", example = "50.00", required = true)
    private float prix;

    @Schema(description = "Identifiant de l'utilisateur auquel le ticket appartient", example = "2", required = true)
    private Long utilisateurId;

    @Schema(description = "Identifiant de l'événement auquel ce ticket correspond", example = "10", required = true)
    private Long evenementId;

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeQR() {
        return codeQR;
    }

    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Long getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(Long evenementId) {
        this.evenementId = evenementId;
    }
}
