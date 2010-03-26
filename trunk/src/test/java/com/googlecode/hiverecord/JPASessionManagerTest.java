package com.googlecode.hiverecord;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JPASessionManagerTest {
	JPASessionManager<Message> O;
	@Mock
	EntityManager manager;
	@Mock
	EntityManagerFactory factory;
	@Mock
	EntityTransaction transaction;
	@Mock
	Query query;

	Message messsage = new Message("expected");

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
	public void shouldBeRollbackedWhenThereAreProblemsWithFind()
			throws Exception {
		when(manager.find(Message.class, 1L)).thenThrow(new RuntimeException());

		try {
			O.find(Message.class, 1L);
		} catch (HiveRecordException e) {
			verify(transaction).rollback();
			throw e;
		}
	}

	@Test(expected=HiveRecordException.class)
	public void shouldBeClosedEvenThoughThereAreProblemsWithFind()
			throws Exception {
		when(manager.find(Message.class, 1L)).thenThrow(new RuntimeException());

		try {
			O.find(Message.class, 1L);
		} catch (HiveRecordException e) {
			verify(manager).close();
			throw e;
		}
	}
	
	@Test
	public void shouldBeCommitedAfterFindingEntity() throws Exception {
		O.find(Message.class, 1L);
		verify(manager).find(Message.class, 1L);
		verify(transaction).commit();
	}

	@Test
	public void shouldBeClosedAfterFindingEntity() throws Exception {
		O.find(Message.class, 1L);
		verify(manager).close();
	}

	@Test(expected = HiveRecordException.class)
	public void shouldBeRollbackedWhenThereAreProblemsWithFindAll()
			throws Exception {
		when(manager.createQuery(anyString()))
				.thenThrow(new RuntimeException());

		try {
			O.findAll(Message.class);
		} catch (Exception e) {
			verify(transaction).rollback();
			throw e;
		}
	}

	@Test(expected = HiveRecordException.class)
	public void shouldBeClosedEvenThoughThereAreProblemsWithFindAll()
			throws Exception {
		when(manager.createQuery(anyString()))
				.thenThrow(new RuntimeException());

		try {
			O.findAll(Message.class);
		} catch (Exception e) {
			verify(manager).close();
			throw e;
		}
	}

	@Test
	public void shouldBeCommitedAfterFindingAllEntity() throws Exception {
		when(manager.createQuery(anyString())).thenReturn(query);
		when(query.getResultList()).thenReturn(new ArrayList<Message>());

		assertThat(O.findAll(Message.class).size(), is(0));

		verify(transaction).commit();
	}


	@Test
	public void shouldBeClosedAfterFindingAllEntity() throws Exception {
		when(manager.createQuery(anyString())).thenReturn(query);
		when(query.getResultList()).thenReturn(new ArrayList<Message>());

		O.findAll(Message.class);

		verify(manager).close();
	}
	
	@Test
	public void openStateShouldBeReturned() {
		when(manager.isOpen()).thenReturn(true);
		assertThat(O.isOpen(), is(true));
		verify(manager).close();
	}

	@Test(expected = HiveRecordException.class)
	public void shouldBeRollbackedWhenThereAreProblemsWithMerge()
			throws Exception {
		when(manager.merge(messsage)).thenThrow(new RuntimeException());

		try {
			O.merge(messsage);
		} catch (Exception e) {
			verify(transaction).rollback();
			throw e;
		}
	}

	@Test(expected = HiveRecordException.class)
	public void shouldBeClosedWhenThereAreProblemsWithMerge()
			throws Exception {
		when(manager.merge(messsage)).thenThrow(new RuntimeException());

		try {
			O.merge(messsage);
		} catch (Exception e) {
			verify(manager).close();
			throw e;
		}
	}

	@Test
	public void shouldBeCommitedAfterMergingEntity() {
		when(manager.merge(messsage)).thenReturn(messsage);
		O.merge(messsage);
		verify(transaction).commit();
		verify(manager).close();
	}

	@Test
	public void shouldBeClosedAfterMergingEntity() {
		O.merge(messsage);
		verify(manager).close();
	}

	@Test(expected = HiveRecordException.class)
	public void shouldBeRollbackedWhenThereAreProblemsWithPersist()
			throws Exception {
		doThrow(new RuntimeException()).when(manager).persist(messsage);

		try {
			O.persist(messsage);
		} catch (Exception e) {
			verify(transaction).rollback();
			throw e;
		}
	}

	@Test(expected = HiveRecordException.class)
	public void shouldBeClosedWhenThereAreProblemsWithPersist()
			throws Exception {
		doThrow(new RuntimeException()).when(manager).persist(messsage);

		try {
			O.persist(messsage);
		} catch (Exception e) {
			verify(manager).close();
			throw e;
		}
	}

	@Test
	public void shouldBeCommitedAfterPersistingEntity() throws Exception {
		O.merge(messsage);
		verify(transaction).commit();
		verify(manager).close();
	}

	@Test
	public void shouldBeClosedAfterPersistingEntity() throws Exception {
		O.merge(messsage);
		verify(manager).close();
	}

	@Test(expected = HiveRecordException.class)
	public void shouldBeRollbackedWhenThereAreProblemsWithRemove()
			throws Exception {
		when(manager.merge(messsage)).thenReturn(messsage);
		doThrow(new RuntimeException()).when(manager).remove(messsage);

		try {
			O.remove(messsage);
		} catch (Exception e) {
			verify(transaction).rollback();
			throw e;
		}
	}

	@Test(expected = HiveRecordException.class)
	public void shouldBeClosedWhenThereAreProblemsWithRemove()
			throws Exception {
		when(manager.merge(messsage)).thenReturn(messsage);
		doThrow(new RuntimeException()).when(manager).remove(messsage);

		try {
			O.remove(messsage);
		} catch (Exception e) {
			verify(manager).close();
			throw e;
		}
	}

	@Test
	public void shouldBeCommitedAfterRemovingEntity() throws Exception {
		O.remove(messsage);
		verify(transaction).commit();
		verify(manager).close();
	}

	@Test
	public void shouldBeClosedAfterRemovingEntity() throws Exception {
		O.remove(messsage);
		verify(manager).close();
	}

	@Test 
	public void shouldBeNotCommitedAfterRollbacked() {
		O.rollbacked = true;
		O.commit();
		verifyNoMoreInteractions(transaction);
	}

	@Test
	public void shouldBeCommited() {
		O.transaction = transaction;
		O.commit();
		verify(transaction).commit();
	}
	
	@Test
	public void rollbackFlagShouldChangeToTrueAfterRollback() {
		assertThat(O.rollbacked, is(false));
		O.rollback(new Exception("SomeException"));
		assertThat(O.rollbacked, is(true));
	}
	
	@Test
	public void managerShouldBeClosed() {
		O.manager = manager;
		O.closeQuietly();
		verify(manager).close();
	}
	
	@Test
	public void entityManagerShouldBeReadyBeforeTransaction() {
		O.ready();
		assertThat(O.manager, is(notNullValue()));
	}
	
	@Test
	public void transactionShouldBeStarted() {
		O.manager = manager;
		O.beginTransaction();
		verify(transaction).begin();
	}
	
	@Test
	public void entityManagerShouldBeClosedWhenFailingBeginTransaction() {
		O.manager = manager;
		O.transaction = transaction;
		when(manager.getTransaction()).thenThrow(new RuntimeException());
		
		try {
			O.beginTransaction();
		} catch (RuntimeException e) {
		}

		verifyNoMoreInteractions(transaction);
		verify(manager).close();			
	}
}
