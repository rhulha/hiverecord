package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class EntitySessionFocusedOnEntityManagerTest {
	private EntityManager manager = mock(EntityManager.class);
	private final Message message = new Message("Hello");
	
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
		Message mergedMessage = (Message)O.merge(message);
		verify(O.entityManager).merge(message);
	}
	
	@Test
	public void shouldBeRemoved() {
		O.remove(message);
		verify(O.entityManager).remove(message);		
	}
	
	@Test
	public void shouldBeFound()  {
		O.find(Message.class, 1L);
		verify(O.entityManager).find(Message.class, 1L);
	}
}
