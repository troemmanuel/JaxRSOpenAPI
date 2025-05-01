package fr.istic.taa.jaxrs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Données retournées après la création ou la récupération d'un profil")
public class ProfilResponseDto {

    @Schema(description = "Identifiant unique de l'utilisateur", example = "1")
    private Long id;

    @Schema(description = "Nom de l'utilisateur", example = "Jean Dupont")
    private String nom;

    @Schema(description = "Adresse email", example = "jean.dupont@example.com")
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
