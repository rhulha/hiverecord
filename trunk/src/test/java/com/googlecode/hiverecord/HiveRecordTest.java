package com.googlecode.hiverecord;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HiveRecordTest {
	EntityManager entityManager = mock(EntityManager.class);
	Query query= mock(Query.class);

	@Test
	public void givenEntityManagerShouldBeUsedWhenPersisting() {
		Message message = new Message();
		message.setEntityManager(entityManager);

		message.persist();

		verify(message.entityManager).persist(message);
	}

	@Test
	public void givenEntityManagerShouldBeUsedWhenRemoving() {
		Message message = new Message();
		message.setEntityManager(entityManager);
		
		message.remove();

		verify(message.entityManager).remove(message);
	}

	@Test
	public void givenEntityManagerShouldBeUsedWhenMerging() {
		Message message = new Message();
		message.setEntityManager(entityManager);

		@SuppressWarnings("unused")
		Message mergedMessage = message.merge();

		verify(message.entityManager).merge(message);
	}

	@Test
	public void givenEntityManagerShouldBeUsedWhenFinding() {
		Message.find(Message.class, 1L, entityManager);
		verify(entityManager).find(Message.class, 1L);
	}

	@Test
	public void givenEntityManagerShouldBeUsedWhenFindingAll() {
		when(entityManager.createQuery(anyString())).thenReturn(query);
		when(query.getSingleResult()).thenReturn(anyObject());
		
		Message.findAll(Message.class, entityManager);
		
		verify(entityManager).createQuery(anyString());;
	}

	@Test
	public void givenEntityManagerShouldBeUsedWhenFindingAllOnTop() {
		when(entityManager.createQuery(anyString())).thenReturn(query);
		when(query.setFirstResult(anyInt())).thenReturn(query);
		when(query.setMaxResults(anyInt())).thenReturn(query);

		Message.findAll(Message.class, 3, Order.desc("someProperty"), entityManager);

		verify(entityManager).createQuery(contains("Message"));
		verify(entityManager).createQuery(contains("someProperty desc"));
		verify(query).setFirstResult(0);
		verify(query).setMaxResults(3);	}
}