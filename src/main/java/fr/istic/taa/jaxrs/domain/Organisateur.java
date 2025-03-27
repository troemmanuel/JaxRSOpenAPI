package fr.istic.taa.jaxrs.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.List;



@Entity
@DiscriminatorValue("ORGANISATEUR")
public class Organisateur extends Profil implements Serializable {
    public List<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }

    @OneToMany(mappedBy = "organisateur")
    private List<Evenement> evenements;
}
