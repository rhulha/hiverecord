package com.googlecode.hiverecord;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.criterion.Order;

public abstract class HiveRecord<T> {
	@PersistenceContext
	protected EntityManager entityManager;	
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void persist() {
		entityManager.persist(this);
	}

	public void remove() {
		entityManager.remove(this);
	}

	@SuppressWarnings("unchecked")
	public T merge() {
		return (T) entityManager.merge(this);
	}

	public static <T> Long count(Class<T> clazz, EntityManager entityManager) {
		return (Long) entityManager.createQuery(
				"SELECT COUNT(o) FROM " + tableName(clazz) + " o")
				.getSingleResult();
	}

	public static <T> T find(Class<T> clazz, Serializable id,
			EntityManager entityManager) {
		return entityManager.find(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> clazz,
			EntityManager entityManager) {
		return entityManager.createQuery(
				"SELECT o FROM " + tableName(clazz) + " o").getResultList();
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> classz, int topCount,
			Order order, EntityManager entityManager) {
		return entityManager.createQuery(
				"SELECT o FROM " + tableName(classz) + " o ORDER BY o."
						+ order.toString()).setFirstResult(0)
				.setMaxResults(topCount).getResultList();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	static String tableName(Class<?> clazz) {
		return clazz.getSimpleName();
	}
}
