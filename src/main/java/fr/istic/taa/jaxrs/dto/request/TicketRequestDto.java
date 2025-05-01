package fr.istic.taa.jaxrs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class TicketRequestDto {

    @Schema(description = "Code QR associé au ticket", example = "qrcode12345", required = true)
    private String codeQR;

    @Schema(description = "État actuel du ticket (ex: valide, annulé)", example = "valide", required = true)
    private String etat;

    @Schema(description = "Prix du ticket", example = "20.5", required = true)
    private float prix;

    @Schema(description = "Identifiant de l'utilisateur ayant acheté le ticket", example = "2", required = true)
    private Long utilisateurId;

    @Schema(description = "Identifiant de l'événement pour lequel le ticket est acheté", example = "3", required = true)
    private Long evenementId;

    // Getters & Setters

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
