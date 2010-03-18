package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;

import com.googlecode.hiverecord.support.Message;

public class SessionManagerFactoryTest {
	@Test(expected = IllegalStateException.class)
	public void exceptionShouldBeThrownForNotifyingUserTo() {
		@SuppressWarnings("unused")
		SessionManager<Message> result = SessionManagerFactory.obtainSessionManager();
	}

	@Test
	public void sessionManagerCanBeObtainedAfterRegisteringSessionFactory() {
		SessionManagerFactory.register(createSessionFactory());

		SessionManager<Message> sessionManager = SessionManagerFactory
				.obtainSessionManager();

		assertThat(sessionManager, is(notNullValue()));
		assertThat(sessionManager.isOpen(), is(true));

		SessionManagerFactory.clear();
	}

	@Test
	public void sessionManagerCanBeObtainedAfterRegisteringEntityManagerFactory() {
		SessionManagerFactory.register(createEntityManagerFactory());

		SessionManager<Message> sessionManager = SessionManagerFactory
				.obtainSessionManager();

		assertThat(sessionManager, is(notNullValue()));
		assertThat(sessionManager.isOpen(), is(true));

		SessionManagerFactory.clear();
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
