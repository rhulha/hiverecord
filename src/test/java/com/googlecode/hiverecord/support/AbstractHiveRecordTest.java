package com.googlecode.hiverecord.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;

import com.googlecode.hiverecord.SessionManagerFactory;

public abstract class AbstractHiveRecordTest {
	protected SessionFactory createSessionFactory() {
		try {
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg = (AnnotationConfiguration) cfg.configure();
			cfg.addAnnotatedClass(Message.class);
			return cfg.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	protected EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("hiverecord");
	}
	
	@Test(expected=IllegalStateException.class)
	public void shouldThrowExceptionWhenSessionFactoryIsNotBeingReady() {
		SessionManagerFactory.unregister();
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
