package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class HiveRecordTest {
	EntitySession entitySession = mock(EntitySession.class);
	
	@Test
	public void canBeCreatedWithCustomTransactionMode() {
		Message message = Message.createWithCustomTransactionMode(Message.class, entitySession);
		
		assertThat(message, is(notNullValue()));
		assertThat(message.entitySession, is(notNullValue()));
	}
}
