package fr.istic.taa.jaxrs.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
@DiscriminatorValue("ADMINISTRATEUR")
public class Administrateur extends Profil implements Serializable {
    public void validerEvenement(Evenement evenement) {
        evenement.setEtat("Validé");
    }
}
