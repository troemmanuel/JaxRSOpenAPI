package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.dao.generic.EntityManagerHelper;
import fr.istic.taa.jaxrs.domain.Evenement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class EvenementDao extends AbstractJpaDao<Long, Evenement> {
    public EvenementDao() {
        setClazz(Evenement.class);
    }public List<Evenement> findByOrganisateurId(Long idOrganisateur) {
        String jpql = "SELECT e FROM Evenement e WHERE e.organisateur.id = :idOrganisateur";
        TypedQuery<Evenement> query = entityManager.createQuery(jpql, Evenement.class);
        query.setParameter("idOrganisateur", idOrganisateur);
        return query.getResultList();

    }
    @Override
    public Evenement update(Evenement evenement) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        em.getTransaction().begin();
        Evenement merged = em.merge(evenement);
        em.getTransaction().commit();
        return merged;
    }


}
