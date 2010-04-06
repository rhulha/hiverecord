package com.googlecode.hiverecord;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class EntitySession {
	EntityManager entityManager;
	EntityTransaction entityTransaction;

	Session session;
	Transaction transaction;

	EntitySession() {
	}

	public EntitySession(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntitySession(Session session) {
		this.session = session;
	}

	public void persist(Object entity) {
		if (entityManager != null) {
			entityManager.persist(entity);
		} else {
			session.persist(entity);
		}
	}

	public Object merge(Object entity) {
		if (entityManager != null) {
			return entityManager.merge(entity);
		} else {
			return session.merge(entity);
		}
	}

	public void remove(Object entity) {
		if (entityManager != null) {
			entityManager.remove(entity);
		} else {
			session.delete(entity);
		}
	}

	public Object find(Class<?> clazz, Serializable id) {
		if (entityManager != null) {
			return entityManager.find(clazz, id);
		} else {
			return session.get(clazz, id);
		}
	}

	public Long count(Class<?> clazz) {
		if (entityManager != null) {
			return (Long) entityManager.createQuery(
					"SELECT COUNT(o) FROM " + tableName(clazz) + " o")
					.getSingleResult();
		} else {
			return (Long) session.createQuery(
					"SELECT COUNT(o) FROM " + tableName(clazz) + " o")
					.uniqueResult();
		}
	}

	public void beginTransaction() {
		if (entityManager != null) {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
		} else {
			transaction = session.beginTransaction();
			transaction.begin();
		}
	}

	public void rollback() {
		if (entityManager != null) {
			entityTransaction.rollback();
		} else {
			transaction.rollback();
		}
	}

	public void close() {
		if (entityManager != null) {
			entityManager.close();
		} else {
			session.close();
		}
	}

	public void commit() {
		if (entityManager != null) {
			entityTransaction.commit();
		} else {
			transaction.commit();
		}
	}

	public List<?> findAll(Class<?> clazz) {
		if (entityManager != null) {
			return entityManager.createQuery(
					"SELECT o FROM " + tableName(clazz) + " o").getResultList();
		} else {
			return session.createCriteria(clazz).list();
		}
	}

	public List<?> findAll(Class<?> clazz, int topCount, Order order) {
		if (entityManager != null) {
			return entityManager.createQuery(
					"SELECT o FROM " + tableName(clazz) + " o ORDER BY o."
							+ order.toString()).setFirstResult(0)
					.setMaxResults(topCount).getResultList();
		} else {
			Criteria criteria = session.createCriteria(clazz);
			criteria.addOrder(order).setFirstResult(0).setMaxResults(topCount);
			return criteria.list();
		}
	}

	String tableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		return table == null ? clazz.getSimpleName() : table.name();
	}
}
