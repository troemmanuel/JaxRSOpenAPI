package fr.istic.taa.jaxrs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Données pour créer ou mettre à jour un profil utilisateur")
public class ProfilRequestDto {

    @Schema(description = "Nom de l'utilisateur", example = "Jean Dupont", required = true)
    private String nom;

    @Schema(description = "Adresse email", example = "jean.dupont@example.com", required = true)
    private String email;

    @Schema(description = "Mot de passe", example = "motdepasse123", required = true)
    private String motDePasse;

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

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
