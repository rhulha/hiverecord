package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.criterion.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JPAHiveRecordTest {
	EntityManager entityManager;
	EntityTransaction transaction;
	@Before
	public void prepareEntityManager() {
		entityManager = createEntityManagerFactory().createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
	}

	@After
	public void closeEntityManager() {
		transaction.commit();
		entityManager.close();
	}

	@Test
	public void nullShouldBeReturnedWhenThereIsNoRow() {
		Message message = Message.find(Message.class, 1L, entityManager);
		assertThat(message, is(nullValue()));
	}

	@Test
	public void entityCanBePersistedAndThenFoundAgain() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.setEntityManager(entityManager);
		message.persist();
		Long id = message.getId();

		assertThat(id, is(notNullValue()));
		assertThat(Message.find(Message.class, id, entityManager), is(notNullValue()));
		assertThat(Message.find(Message.class, id, entityManager).getMessage(), is(message
				.getMessage()));
	}

	@Test
	public void entityCanBeMerged() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.setEntityManager(entityManager);
		message.persist();

		Long id = message.getId();
		message.setMessage("Modifed");
		Message mergedMessage = message.merge();

		assertThat(mergedMessage.getMessage(), is(message.getMessage()));
		assertThat(Message.find(Message.class, id, entityManager).getMessage(), is(message
				.getMessage()));
	}

	@Test
	public void allEntitiesCanBeObtainedWhenThereIsNoRow() {
		List<Message> messages = Message.findAll(Message.class, entityManager);
		assertThat(messages.size(), is(0));
	}

	@Test
	public void allEntitiesCanBeObtainedWhenThereAreSomeRows() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.setEntityManager(entityManager);
		message.persist();
		message = new Message("Hello " + System.currentTimeMillis());
		message.setEntityManager(entityManager);
		message.persist();

		List<Message> messages = Message.findAll(Message.class, entityManager);
		assertThat(messages.size(), is(2));
	}

	@Test
	public void entityCanBeCounted() {
		long count = 6;
		for (int i = 0; i < count; i++) {
			Message message = new Message(String.valueOf(i));
			message.setEntityManager(entityManager);
			message.persist();
		}

		assertThat(Message.count(Message.class, entityManager), is(count));
	}

	@Test
	public void entitiesOnTopCanBeObtainedWithAsc() {
		insertEntities(5);

		List<Message> result = Message.findAll(Message.class, 2, Order
				.asc("id"), entityManager);

		assertThat(result.size(), is(2));
		assertThat(result.get(0).getMessage(), is("1"));
		assertThat(result.get(1).getMessage(), is("2"));
	}

	@Test
	public void entitiesOnTopCanBeObtainedWithDesc() {
		insertEntities(5);

		List<Message> result = Message.findAll(Message.class, 3, Order
				.desc("id"), entityManager);

		assertThat(result.size(), is(3));
		assertThat(result.get(0).getMessage(), is("5"));
		assertThat(result.get(1).getMessage(), is("4"));
		assertThat(result.get(2).getMessage(), is("3"));
	}

	@Test
	public void findAllWithTopShouldReturnEmptyListOfEntitiesIfThereIsNoEntity() {
		List<Message> result = Message.findAll(Message.class, 3, Order
				.desc("id"), entityManager);

		assertThat(result.size(), is(0));
	}

	@Test
	public void entityCanBeRemoved() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.setEntityManager(entityManager);
		message.persist();

		message.remove();

		assertThat(Message.findAll(Message.class, entityManager).size(), is(0));
	}

	private void insertEntities(int count) {
		for (int i = 1; i <= count; i++) {
			Message message = new Message(String.valueOf(i));
			message.setEntityManager(entityManager);
			message.persist();
		}
	}

	private EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("hiverecord");
	}
}
