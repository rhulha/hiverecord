package com.googlecode.hiverecord;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.Session;


public class EntitySession {
	EntityManager entityManager;
	Session session;

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

	public void remove(Message message) {
		if (entityManager != null) {
			entityManager.remove(message);
		} else {
			session.delete(message);
		}
	}

	public Object find(Class<?> clazz, Serializable id) {
		if (entityManager != null) {
			return entityManager.find(clazz, id);
		} else {
			return session.get(clazz, id);
		}
	}
}
