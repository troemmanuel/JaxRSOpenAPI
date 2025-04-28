package fr.istic.taa.jaxrs.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.List;

@Entity
@DiscriminatorValue("UTILISATEUR")
public class Utilisateur extends Profil implements Serializable {
    @OneToMany(mappedBy = "utilisateur")
    private List<Ticket> tickets;

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}