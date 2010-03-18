package com.googlecode.hiverecord;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SessionManagerFactory {
	private static SessionFactory sessionFactory;
	private static EntityManagerFactory entityManagerFactory;

	/**
	 * @deprecated
	 * @see #obtainSessionManager
	 */
	public static Session openSession() {
		if (sessionFactory == null) {
			throw new IllegalStateException(
					"You should register Hibernate SessionFactory to "
							+ SessionManagerFactory.class.toString());
		}

		return sessionFactory.openSession();
	}

	public static void register(EntityManagerFactory entityManagerFactory) {
		SessionManagerFactory.entityManagerFactory = entityManagerFactory; 
	}
	
	public static void register(SessionFactory sessionFactory) {
		SessionManagerFactory.sessionFactory = sessionFactory;
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

	public static <T> SessionManager<T> obtainSessionManager() {
		if (sessionFactory == null && entityManagerFactory == null) {
			throw new IllegalStateException(
					"You should register either SessionFactory or EntityManagerFactory.");
		}
		
		if (sessionFactory != null) {
			return new HibernateSessionManager<T>(sessionFactory);
		} else {
			return new JPASessionManager<T>(entityManagerFactory);			
		}
	}
}
