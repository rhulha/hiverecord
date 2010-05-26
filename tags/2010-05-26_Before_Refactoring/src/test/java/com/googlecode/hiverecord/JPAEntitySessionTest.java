package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.googlecode.hiverecord.support.EntitySessionTestScenario;

@RunWith(MockitoJUnitRunner.class)
public class JPAEntitySessionTest implements EntitySessionTestScenario {
	EntityManager manager = mock(EntityManager.class);
	EntityTransaction entityTransaction = mock(EntityTransaction.class);
	final Message message = new Message("Hello");

	EntitySession O;

	@Before
	public void ready() {
		O = new EntitySession(manager);
	}

	@Test
	public void shouldBeCreatedWithSession() {
		assertThat(O.entityManager, is(manager));
	}

	@Test
	public void shouldBePersisted() {
		O.persist(message);
		verify(O.entityManager).persist(message);
	}

	@Test
	public void shouldBeMerged() {
		@SuppressWarnings("unused")
		Message mergedMessage = (Message) O.merge(message);
		verify(O.entityManager).merge(message);
	}

	@Test
	public void shouldBeRemoved() {
		O.remove(message);
		verify(O.entityManager).remove(message);
	}

	@Test
	public void shouldBeFound() {
		O.find(Message.class, 1L);
		verify(O.entityManager).find(Message.class, 1L);
	}

	@Test
	public void transactionShouldBeBegun() {
		when(manager.getTransaction()).thenReturn(entityTransaction);

		O.beginTransaction();

		assertThat(O.entityTransaction, is(notNullValue()));
		verify(entityTransaction).begin();
	}

	@Test
	public void transactionCanBeRollbacked() {
		O.entityTransaction = entityTransaction;
		O.rollback();
		verify(O.entityTransaction).rollback();
	}

	@Test
	public void canBeClosedSafely() {
		O.close();
		verify(O.entityManager).close();
	}

	@Test
	public void transactionCanBeCommitted() {
		O.entityTransaction = entityTransaction;
		O.commit();
		verify(O.entityTransaction).commit();
	}

	@Test
	public void allEntitiesShouldBeFound() {
		Query query = mock(Query.class);
		when(manager.createQuery(anyString())).thenReturn(query);
		when(query.getResultList()).thenReturn(new ArrayList<Message>());

		O.findAll(Message.class);
		verify(O.entityManager).createQuery(anyString());
	}
}
