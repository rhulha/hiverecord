package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.junit.Test;

public class CustomTransactionSupportTest {
	EntitySession entitySession = mock(EntitySession.class);

	EntityManager entityManager = mock(EntityManager.class);
	Session session = mock(Session.class);

	@Test
	public void canBeCreatedWithCustomTransactionMode() {
		Message message = Message.createWithCustomTransactionMode(
				Message.class, entitySession);
		verifyWhetherEntitySessionExistsOrNot(message);
	}

	@Test
	public void canBeCreatedWithCustomTransactionModeByEntityManager() {
		Message message = Message.createWithCustomTransactionMode(
				Message.class, entityManager);
		verifyWhetherEntitySessionExistsOrNot(message);
	}

	@Test
	public void canBeCreatedWithCustomTransactionModeBySession() {
		Message message = Message.createWithCustomTransactionMode(
				Message.class, session);
		verifyWhetherEntitySessionExistsOrNot(message);
	}

	private void verifyWhetherEntitySessionExistsOrNot(Message message) {
		assertThat(message, is(notNullValue()));
		assertThat(message.customEntitySession, is(notNullValue()));
	}
}
