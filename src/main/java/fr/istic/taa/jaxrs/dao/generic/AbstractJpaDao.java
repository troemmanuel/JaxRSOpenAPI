package fr.istic.taa.jaxrs.dao.generic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public abstract class AbstractJpaDao<K, T extends Serializable> implements IGenericDao<K, T> {

	private Class<T> clazz;

	protected EntityManager entityManager;

	public AbstractJpaDao() {
		this.entityManager = EntityManagerHelper.getEntityManager();
	}

	public void setClazz(Class<T> clazzToSet) {
		this.clazz = clazzToSet;
	}

	public T findOne(K id) {
		return entityManager.find(clazz, id);
	}

	public List<T> findBy(Map<String, Object> properties) {
		if (properties == null || properties.isEmpty()) {
			throw new IllegalArgumentException("Properties map must not be null or empty.");
		}

		StringBuilder queryBuilder = new StringBuilder("SELECT e FROM ");
		queryBuilder.append(clazz.getName()).append(" e WHERE ");

		int index = 0;
		for (String property : properties.keySet()) {
			if (index > 0) {
				queryBuilder.append(" AND ");
			}
			queryBuilder.append("e.").append(property).append(" = :").append(property);
			index++;
		}

		TypedQuery<T> query = entityManager.createQuery(queryBuilder.toString(), clazz);
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		return query.getResultList();
	}


	public List<T> findAll() {
		return entityManager.createQuery("select e from " + clazz.getName() + " as e",clazz).getResultList();
	}

	public void save(T entity) {
		EntityTransaction t = this.entityManager.getTransaction();
		t.begin();
		entityManager.persist(entity);
		t.commit();

	}

	public T update(final T entity) {
		EntityTransaction t = this.entityManager.getTransaction();
		t.begin();
		T res = entityManager.merge(entity);
		t.commit();
		return res;

	}

	public void delete(T entity) {
		EntityTransaction t = this.entityManager.getTransaction();
		t.begin();
		entityManager.remove(entity);
		t.commit();

	}

	public void deleteById(K entityId) {
		T entity = findOne(entityId);
		delete(entity);
	}
}
