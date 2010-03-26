package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;



public class EntitySessionFactoryTest {
	@Test(expected = IllegalStateException.class)
	public void exceptionShouldBeThrownWhenEntitySessionFactoryIsNotBeingReady() {
		@SuppressWarnings("unused")
		EntitySession result = EntitySessionFactory.obtainEntitySession();
	}

	@Test
	public void entitySessionCanBeObtainedAfterRegisteringSessionFactory() {
		EntitySessionFactory.register(createSessionFactory());
		EntitySession entitySession = EntitySessionFactory.obtainEntitySession();

		assertThat(entitySession, is(notNullValue()));

		EntitySessionFactory.clear();
	}

	@Test
	public void entitySessionCanBeObtainedAfterRegisteringEntityManagerFactory() {
		EntitySessionFactory.register(createEntityManagerFactory());
		EntitySession entitySession = EntitySessionFactory.obtainEntitySession();
		
		assertThat(entitySession, is(notNullValue()));

		EntitySessionFactory.clear();
	}
	
	private SessionFactory createSessionFactory() {
		try {
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg = (AnnotationConfiguration) cfg.configure();
			cfg.addAnnotatedClass(Message.class);
			return cfg.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	private EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("hiverecord");
	}
}
