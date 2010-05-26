package com.googlecode.hiverecord.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.criterion.Order;
import org.junit.Test;

import com.googlecode.hiverecord.EntitySessionFactory;
import com.googlecode.hiverecord.Message;

public abstract class AbstractHiveRecordTest extends AbstractFactory {
	@Test(expected = IllegalStateException.class)
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
		assertThat(Message.find(Message.class, id).getMessage(), is(message
				.getMessage()));
	}

	@Test
	public void entityCanBeMerged() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.persist();

		Long id = message.getId();
		message.setMessage("Modifed");
		Message mergedMessage = message.merge();

		assertThat(mergedMessage.getMessage(), is(message.getMessage()));
		assertThat(Message.find(Message.class, id).getMessage(), is(message
				.getMessage()));
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
	public void entityCanBeCounted() {
		long count = 6;
		for (int i = 0; i < count; i++) {
			new Message(String.valueOf(i)).persist();
		}
		assertThat(Message.count(Message.class), is(count));

		new Message("One more").persist();
		assertThat(Message.count(Message.class), is(count + 1));
	}

	@Test
	public void entitiesOnTopCanBeObtainedWithAsc() {
		insertEntities(5);

		List<Message> result = Message.findAll(Message.class, 2, Order
				.asc("id"));

		assertThat(result.size(), is(2));
		assertThat(result.get(0).getMessage(), is("1"));
		assertThat(result.get(1).getMessage(), is("2"));
	}

	@Test
	public void entitiesOnTopCanBeObtainedWithDesc() {
		insertEntities(5);

		List<Message> result = Message.findAll(Message.class, 3, Order
				.desc("id"));

		assertThat(result.size(), is(3));
		assertThat(result.get(0).getMessage(), is("5"));
		assertThat(result.get(1).getMessage(), is("4"));
		assertThat(result.get(2).getMessage(), is("3"));
	}

	@Test
	public void findAllWithTopShouldReturnEmptyListOfEntitiesIfThereIsNoEntity() {
		List<Message> result = Message.findAll(Message.class, 3, Order
				.desc("id"));

		assertThat(result.size(), is(0));
	}

	@Test
	public void entityCanBeRemoved() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.persist();

		message.remove();

		assertThat(Message.findAll(Message.class).size(), is(0));
	}

	private void insertEntities(int count) {
		for (int i = 1; i <= count; i++) {
			new Message(String.valueOf(i)).persist();
		}
	}
}
