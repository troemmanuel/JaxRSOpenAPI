package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Administrateur;

public class AdministrateurDao extends AbstractJpaDao<Long, Administrateur> {
    public AdministrateurDao() {
        setClazz(Administrateur.class);
    }
}
