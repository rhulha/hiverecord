package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.googlecode.hiverecord.support.Pizza;

@RunWith(MockitoJUnitRunner.class)
public class EntitySessionTest {
	EntitySession O = new EntitySession();
	Message message = new Message("Sample");
	
	@Mock
	EntityManager entityManager;
	@Mock
	Session session;
	@Mock
	Query query;
	@Mock
	Criteria criteria;

	@Test
	public void tablaNameShouldBeObtainedFromEntity() {
		assertThat(O.tableName(Message.class), is("Message"));
		assertThat(O.tableName(Pizza.class), is("OhPizza"));
	}
	
	@Test
	public void entityManagerShouldBeUserWhenCounting() {
		O.entityManager = entityManager;
		verifyNoMoreInteractions(session);
	}
	
	@Test
	public void parametersShouldBeAppliedCorrectlyToQueryWithEntityManagerWhenFindAllOnTop() {
		when(entityManager.createQuery(anyString())).thenReturn(query);
		when(query.setFirstResult(anyInt())).thenReturn(query);
		when(query.setMaxResults(anyInt())).thenReturn(query);
		O.entityManager = entityManager;

		O.findAll(Message.class, 3, Order.desc("someProperty"));

		verify(entityManager).createQuery(contains("Message"));
		verify(entityManager).createQuery(contains("someProperty desc"));
		verify(query).setFirstResult(0);
		verify(query).setMaxResults(3);
	}

	@Test
	public void parametersShouldBeAppliedCorrectlyToQueryWithSessionWhenFindAllOnTop() {
		Order order = Order.desc("someProperty");
		when(session.createCriteria(Message.class)).thenReturn(criteria);
		O.session = session;

		O.findAll(Message.class, 3, order);

		verify(criteria).addOrder(order);
		verify(criteria).setFirstResult(0);
		verify(criteria).setMaxResults(3);
		verify(criteria).list();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void entityManagerShouldBeOnlyUsedInCRUD() {
		O.entityManager = entityManager;
		invokeAllIncludedCRUD();
		
		verify(entityManager).persist(anyObject());
		verify(entityManager).merge(anyObject());
		verify(entityManager).remove(anyObject());
		verify(entityManager).find(any(Class.class), anyObject());
		verifyZeroInteractions(session);
	}

	@Test
	public void sessionShouldBeOnlyUsedInCRUD() {
		O.session= session;
		invokeAllIncludedCRUD();
		verify(session).persist(anyObject());
		verify(session).merge(anyObject());
		verify(session).delete(anyObject());
		verify(session).get(any(Class.class), (Serializable) anyObject());
		verifyZeroInteractions(entityManager);
	}
	
	private void invokeAllIncludedCRUD() {
		O.persist(message);
		O.merge(message);
		O.remove(message);
		O.find(Message.class, 1L);
		try {
			O.count(Message.class);
		} catch (NullPointerException e) {
		}
		try {
			O.findAll(Message.class);
		} catch (NullPointerException e) {
		}
		try {
			O.findAll(Message.class, 1, Order.desc("someProperty"));
		} catch (NullPointerException e) {
		}
	}
}
