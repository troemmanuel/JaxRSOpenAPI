package fr.istic.taa.jaxrs.dto.request;

public class TicketRequestDto {
    private String codeQR;
    private String etat;
    private float prix;
    private Long utilisateurId;
    private Long evenementId;

    // Getters & Setters

    public String getCodeQR() {
        return codeQR;
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

    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }
}
