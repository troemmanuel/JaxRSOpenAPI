package fr.istic.taa.jaxrs.dto.mapper;

import fr.istic.taa.jaxrs.domain.Administrateur;
import fr.istic.taa.jaxrs.domain.Organisateur;
import fr.istic.taa.jaxrs.domain.Profil;
import fr.istic.taa.jaxrs.domain.Utilisateur;
import fr.istic.taa.jaxrs.dto.request.ProfilRequestDto;
import fr.istic.taa.jaxrs.dto.response.ProfilResponseDto;

public class ProfilMapper {


    public static <T extends Profil> void updateEntity(T profil, ProfilRequestDto dto) {
        profil.setNom(dto.getNom());
        profil.setEmail(dto.getEmail());
        profil.setMotDePasse(dto.getMotDePasse());
    }

    public static ProfilResponseDto toDto(Profil profil) {
        ProfilResponseDto dto = new ProfilResponseDto();
        dto.setId(profil.getId());
        dto.setNom(profil.getNom());
        dto.setEmail(profil.getEmail());
        return dto;
    }

    public static Administrateur toAdministrateurEntity(ProfilRequestDto dto) {
        Administrateur admin = new Administrateur();
        admin.setNom(dto.getNom());
        admin.setEmail(dto.getEmail());
        admin.setMotDePasse(dto.getMotDePasse());
        return admin;
    }

    public static Utilisateur toUtilisateurEntity(ProfilRequestDto dto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(dto.getMotDePasse());
        return utilisateur;
    }

    public static Organisateur toOrganisateurEntity(ProfilRequestDto dto) {
        Organisateur organisateur = new Organisateur();
        organisateur.setNom(dto.getNom());
        organisateur.setEmail(dto.getEmail());
        organisateur.setMotDePasse(dto.getMotDePasse());
        return organisateur;
    }
}
