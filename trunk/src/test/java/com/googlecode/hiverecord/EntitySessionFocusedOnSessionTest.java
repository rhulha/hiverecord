package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class EntitySessionFocusedOnSessionTest {
	private Session session = mock(Session.class);
	private final Message message = new Message("Hello");
	
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
		Message mergedMessage = (Message)O.merge(message);
		verify(O.session).merge(message);
	}
	
	@Test
	public void shouldBeRemoved() {
		O.remove(message);
		verify(O.session).delete(message);		
	}
	
	@Test
	public void shouldBeFound()  {
		O.find(Message.class, 1L);
		verify(O.session).get(Message.class, 1L);
	}
}
