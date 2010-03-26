package com.googlecode.hiverecord;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class EntitySession {
	EntityManager entityManager;
	EntityTransaction entityTransaction;

	Session session;
	Transaction transaction;

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
			return entityManager.createQuery("SELECT o FROM " + clazz.getName() + " o").getResultList();
		} else {
			return session.createCriteria(clazz).list();
		}		
	}
}
