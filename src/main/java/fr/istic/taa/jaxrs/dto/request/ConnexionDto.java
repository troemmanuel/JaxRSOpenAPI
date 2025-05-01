package fr.istic.taa.jaxrs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class ConnexionDto {

    @Schema(description = "Adresse email de l'utilisateur", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "Mot de passe de l'utilisateur", example = "password123", required = true)
    private String motDePasse;

    // Getters et Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
