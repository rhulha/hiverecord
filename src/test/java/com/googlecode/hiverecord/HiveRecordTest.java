package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HiveRecordTest {
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

	@Test
	public void givenEntitySessionShouldBeUsedWhenPersisting() {
		Message message = Message.createWithCustomTransactionMode(
				Message.class, entitySession);

		message.persist();

		verify(message.customEntitySession).persist(message);
	}

	@Test
	public void givenEntitySessionShouldBeUsedWhenRemoving() {
		Message message = Message.createWithCustomTransactionMode(
				Message.class, entitySession);

		message.remove();

		verify(message.customEntitySession).remove(message);
	}

	@Test
	public void givenEntitySessionShouldBeUsedWhenMerging() {
		Message message = Message.createWithCustomTransactionMode(
				Message.class, entitySession);

		@SuppressWarnings("unused")
		Message mergedMessage = message.merge();

		verify(message.customEntitySession).merge(message);
	}

	@Test
	public void givenEntitySessionShouldBeUsedWhenFinding() {
		Message.find(Message.class, 1L, entitySession);
		verify(entitySession).find(Message.class, 1L);
	}

	@Test
	public void givenEntitySessionShouldBeUsedWhenFindingAll() {
		Message.findAll(Message.class, entitySession);
		verify(entitySession).findAll(Message.class);
	}

	private void verifyWhetherEntitySessionExistsOrNot(Message message) {
		assertThat(message, is(notNullValue()));
		assertThat(message.customEntitySession, is(notNullValue()));
	}
}