package com.googlecode.hiverecord;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.googlecode.hiverecord.support.Message;

@RunWith(MockitoJUnitRunner.class)
public class JPASessionManagerTest {
	JPASessionManager<Message> O;
	@Mock
	EntityManager manager;
	@Mock
	EntityManagerFactory factory;
	@Mock
	EntityTransaction transaction;
	
	@Before
	public void prepereFactoryMock() {
		when(factory.createEntityManager()).thenReturn(manager);
		O = new JPASessionManager<Message>(factory);
	}

	@Before
	public void prepereTransactionMock() {
		when(manager.getTransaction()).thenReturn(transaction);
	}
	
	@Test(expected=HiveRecordException.class)
	public void transactionShouldBeRollbackedAndClosedWhenCanNotFind() throws Exception {
		when(manager.find(Message.class, 1L)).thenThrow(new RuntimeException());
		
		try {
			O.find(Message.class, 1L);
		} catch (Exception e) {
			throw e;
		} finally {
			assertThat(O.rollbacked, is(true));
			verify(manager).close();
		}
	}

	@Test
	public void transactionShouldBeCommitedAndClosedWhenCanFind() throws Exception {
		Message expectedMesssage = new Message("expected");
		when(manager.find(Message.class, 1L)).thenReturn(expectedMesssage);
		
		Message result = O.find(Message.class, 1L);
		
		assertThat(result, equalTo(expectedMesssage));
		verify(transaction).commit();
		verify(manager).close();
	}
}
