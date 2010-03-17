package com.googlecode.hiverecord;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HiveRecordSessionFactory {
	private static SessionFactory sessionFactory;

	public static Session openSession() {
		if (sessionFactory == null) {
			throw new IllegalStateException(
					"You should register Hibernate SessionFactory to "
							+ HiveRecordSessionFactory.class.toString());
		}

		return sessionFactory.openSession();
	}

	public static void register(SessionFactory sessionFactory) {
		HiveRecordSessionFactory.sessionFactory = sessionFactory;
	}
	static void clear() {
		sessionFactory.close();
		sessionFactory = null;
	}
	
	public static void unregister() {
		sessionFactory = null;
	}
}
