package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.hiverecord.support.AbstractHiveRecordTest;
import com.googlecode.hiverecord.support.Message;

public class HiveRecordTest extends AbstractHiveRecordTest {
	HiveRecord<Message> message = new Message();
	
	@Before
	public void readyHiveRecordSessionFactory() {
		HiveRecordSessionFactory.register(createSessionFactory());		
	}

	@After
	public void clearHiveRecordSessionFactory() {
		HiveRecordSessionFactory.unregister();
	}

	@Test(expected=IllegalStateException.class)
	public void activeRecordShouldThrowExceptionWhenSessionFactoryIsNotBeingReady() {
		HiveRecordSessionFactory.unregister();
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
		Long id = (Long)message.save();
		
		assertThat(id, is(notNullValue()));
		assertThat(Message.find(Message.class, id), is(notNullValue()));
		assertThat(Message.find(Message.class, id).getMessage(), is(message.getMessage()));
	}

	@Test
	public void entityCanBeMerged() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		Long id = (Long)message.save();
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
		message.save();
		message = new Message("Hello " + System.currentTimeMillis());
		message.save();
		
		List<Message> messages = Message.findAll(Message.class);
		assertThat(messages.size(), is(2));
	}
	
	@Test
	public void entityCanBeRemoved() {
		Message message = new Message("Hello " + System.currentTimeMillis());
		message.save();
		
		message.remove();

		assertThat(Message.findAll(Message.class).size(), is(0));
	}
}
