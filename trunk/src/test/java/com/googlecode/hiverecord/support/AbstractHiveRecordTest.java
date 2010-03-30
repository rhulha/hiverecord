package com.googlecode.hiverecord.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.googlecode.hiverecord.EntitySessionFactory;
import com.googlecode.hiverecord.Message;

public abstract class AbstractHiveRecordTest extends AbstractFactory {
	@Test(expected=IllegalStateException.class)
	public void shouldThrowExceptionWhenSessionFactoryIsNotBeingReady() {
		EntitySessionFactory.unregister();
		Message.find(Message.class, 1L);
	}
	
	@Test
	public void nullShouldBeReturnedWhenThereIsNoRow() {
		Message message = Message.find(Message.class, 1L);
		assertThat(message, is(nullValue()));
	}
	
	@Test
	public void entityCanBePersistedAndThenFoundAgain() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.persist();
		Long id = message.getId();
		
		assertThat(id, is(notNullValue()));
		assertThat(Message.find(Message.class, id), is(notNullValue()));
		assertThat(Message.find(Message.class, id).getMessage(), is(message.getMessage()));
	}

	@Test
	public void entityCanBeMerged() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.persist();
		
		Long id = message.getId();
		message.setMessage("Modifed");
		Message mergedMessage = message.merge();
		
		assertThat(mergedMessage.getMessage(), is(message.getMessage()));
		assertThat(Message.find(Message.class, id).getMessage(), is(message.getMessage()));		
	}
	
	@Test
	public void allEntitiesCanBeObtainedWhenThereIsNoRow() {
		List<Message> messages = Message.findAll(Message.class);
		assertThat(messages.size(), is(0));
	}

	@Test
	public void allEntitiesCanBeObtainedWhenThereAreSomeRows() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.persist();
		message = new Message("Hello " + System.currentTimeMillis());
		message.persist();
		
		List<Message> messages = Message.findAll(Message.class);
		assertThat(messages.size(), is(2));
	}
	
	@Test
	public void entityCanBeRemoved() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.persist();
		
		message.remove();

		assertThat(Message.findAll(Message.class).size(), is(0));
	}
}
