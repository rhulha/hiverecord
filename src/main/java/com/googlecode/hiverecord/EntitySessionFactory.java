package com.googlecode.hiverecord;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;

public class EntitySessionFactory {
	private static SessionFactory sessionFactory;
	private static EntityManagerFactory entityManagerFactory;

	public static void register(EntityManagerFactory entityManagerFactory) {
		EntitySessionFactory.entityManagerFactory = entityManagerFactory;
	}

	public static void register(SessionFactory sessionFactory) {
		EntitySessionFactory.sessionFactory = sessionFactory;
	}

	static void clear() {
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
		}

		if (entityManagerFactory != null) {
			entityManagerFactory.close();
			entityManagerFactory = null;
		}
	}

	public static void unregister() {
		sessionFactory = null;
		entityManagerFactory = null;
	}

	public static EntitySession obtainEntitySession() {
		if (sessionFactory == null && entityManagerFactory == null) {
			throw new IllegalStateException(
					"You should register either SessionFactory or EntityManagerFactory.");
		}

		if (sessionFactory != null) {
			return new EntitySession(sessionFactory.openSession());
		} else {
			return new EntitySession(entityManagerFactory.createEntityManager());
		}
	}
}
