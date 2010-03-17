package com.googlecode.hiverecord.support;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public abstract class AbstractHiveRecordTest {
	protected SessionFactory createSessionFactory() {
		try {
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg = (AnnotationConfiguration) cfg.configure();
			cfg.addAnnotatedClass(Message.class);
			return cfg.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
}
