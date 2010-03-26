package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.googlecode.hiverecord.support.EntitySessionTestScenario;

@RunWith(MockitoJUnitRunner.class)
public class HibernateEntitySessionTest implements
		EntitySessionTestScenario {
	Session session = mock(Session.class);
	Transaction transaction = mock(Transaction.class);
	final Message message = new Message("Hello");

	EntitySession O;

	@Before
	public void ready() {
		O = new EntitySession(session);
	}

	@Test
	public void shouldBeCreatedWithSession() {
		assertThat(O.session, is(session));
	}

	@Test
	public void shouldBePersisted() {
		O.persist(message);
		verify(O.session).persist(message);
	}

	@Test
	public void shouldBeMerged() {
		@SuppressWarnings("unused")
		Message mergedMessage = (Message) O.merge(message);
		verify(O.session).merge(message);
	}

	@Test
	public void shouldBeRemoved() {
		O.remove(message);
		verify(O.session).delete(message);
	}

	@Test
	public void shouldBeFound() {
		O.find(Message.class, 1L);
		verify(O.session).get(Message.class, 1L);
	}

	@Test
	public void transactionShouldBeBegun() {
		when(session.beginTransaction()).thenReturn(transaction);

		O.beginTransaction();

		assertThat(O.session, is(notNullValue()));
		verify(transaction).begin();
	}

	@Test
	public void transactionCanBeRollbacked() {
		O.transaction = transaction;
		O.rollback();
		verify(O.transaction).rollback();		
	}

	@Test
	public void canBeClosedSafely() {
		O.close();
		verify(O.session).close();
	}

	@Test
	public void transactionCanBeCommitted() {
		O.transaction = transaction;
		O.commit();
		verify(O.transaction).commit();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void allEntitiesShouldBeFound() {
		Criteria criteria = mock(Criteria.class);
		when(session.createCriteria(Message.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(new ArrayList<Message>());
		
		List<Message> result = (List<Message>) O.findAll(Message.class);
		
		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(0));
		verify(O.session).createCriteria(Message.class);		
	}
}