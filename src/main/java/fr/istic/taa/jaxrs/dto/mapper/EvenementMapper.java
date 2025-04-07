package fr.istic.taa.jaxrs.dto.mapper;

import fr.istic.taa.jaxrs.domain.Evenement;
import fr.istic.taa.jaxrs.domain.Organisateur;
import fr.istic.taa.jaxrs.dto.TicketShortDto;
import fr.istic.taa.jaxrs.dto.request.EvenementRequestDto;
import fr.istic.taa.jaxrs.dto.response.EvenementResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EvenementMapper {

    public static Evenement toEntity(EvenementRequestDto dto, Organisateur organisateur) {
        Evenement e = new Evenement();
        e.setNom(dto.getNom());
        e.setDate(dto.getDate());
        e.setLieu(dto.getLieu());
        e.setCapacite(dto.getCapacite());
        e.setDescription(dto.getDescription());
        e.setEtat(dto.getEtat());
        e.setStock(dto.getStock());
        e.setGenre(dto.getGenre());
        e.setArtiste(dto.getArtiste());
        e.setOrganisateur(organisateur);
        return e;
    }

    public static EvenementResponseDto toDto(Evenement e) {
        EvenementResponseDto dto = new EvenementResponseDto();
        dto.setId(e.getId());
        dto.setNom(e.getNom());
        dto.setDate(e.getDate());
        dto.setLieu(e.getLieu());
        dto.setCapacite(e.getCapacite());
        dto.setDescription(e.getDescription());
        dto.setEtat(e.getEtat());
        dto.setStock(e.getStock());
        dto.setGenre(e.getGenre());
        dto.setArtiste(e.getArtiste());
        dto.setOrganisateurId(e.getOrganisateur() != null ? e.getOrganisateur().getId() : null);

        if (e.getTickets() != null) {
            List<TicketShortDto> ticketDtos = e.getTickets().stream()
                    .map(EvenementMapper::toTicketShortDto)
                    .collect(Collectors.toList());
            dto.setTickets(ticketDtos);
        } else {
            dto.setTickets(Collections.emptyList());
        }

        return dto;
    }

    private static TicketShortDto toTicketShortDto(fr.istic.taa.jaxrs.domain.Ticket ticket) {
        TicketShortDto dto = new TicketShortDto();
        dto.setId(ticket.getId());
        dto.setCodeQR(ticket.getCodeQR());
        dto.setEtat(ticket.getEtat());
        dto.setPrix(ticket.getPrix());
        return dto;
    }

}

