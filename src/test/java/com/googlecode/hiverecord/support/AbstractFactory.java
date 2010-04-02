package com.googlecode.hiverecord.support;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.googlecode.hiverecord.Message;

public abstract class AbstractFactory {
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

	protected EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("hiverecord");
	}

}