package fr.istic.taa.jaxrs.dto.mapper;

import fr.istic.taa.jaxrs.domain.Evenement;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.Utilisateur;
import fr.istic.taa.jaxrs.dto.request.TicketRequestDto;
import fr.istic.taa.jaxrs.dto.response.TicketResponseDto;

public class TicketMapper {

    public static Ticket toEntity(TicketRequestDto dto, Utilisateur utilisateur, Evenement evenement) {
        Ticket ticket = new Ticket();
        ticket.setCodeQR(dto.getCodeQR());
        ticket.setEtat(dto.getEtat());
        ticket.setPrix(dto.getPrix());
        ticket.setUtilisateur(utilisateur);
        ticket.setEvenement(evenement);
        return ticket;
    }

    public static TicketResponseDto toDto(Ticket ticket) {
        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(ticket.getId());
        dto.setCodeQR(ticket.getCodeQR());
        dto.setEtat(ticket.getEtat());
        dto.setPrix(ticket.getPrix());
        // Vérification si l'utilisateur est non-null avant de tenter d'accéder à son ID
        if (ticket.getUtilisateur() != null) {
            dto.setUtilisateurId(ticket.getUtilisateur().getId());
        } else {
            dto.setUtilisateurId(null);
        }

        // Vérification si l'événement est non-null avant de tenter d'accéder à son ID
        if (ticket.getEvenement() != null) {
            dto.setEvenementId(ticket.getEvenement().getId());
        } else {
            dto.setEvenementId(null);
        }
        return dto;
    }
}

