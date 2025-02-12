package fr.istic.taa.jaxrs.domain;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "profil_type", discriminatorType = DiscriminatorType.STRING)
abstract class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // AUTO au lieu de IDENTITY
    private Long id;

    private String nom;
    private String email;
    @Column(name = "mot_de_passe")
    private String motDePasse;

}