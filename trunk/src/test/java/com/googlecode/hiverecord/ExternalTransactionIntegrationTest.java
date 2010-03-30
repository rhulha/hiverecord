package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.hiverecord.support.AbstractFactory;

public class ExternalTransactionIntegrationTest extends AbstractFactory {
	EntityManagerFactory entityManagerFactory;
	
	@Before
	public void readyHiveRecordSessionFactory() {
		entityManagerFactory = createEntityManagerFactory();
		EntitySessionFactory.register(entityManagerFactory);
	}

	@After
	public void clearHiveRecordSessionFactory() {
		EntitySessionFactory.unregister();
	}
	
	@Test
	public void shouldBeRollbackedAccrodingToExternalTransaction() {
		EntityManager entityManager = null;
		EntityTransaction transaction = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			
			entityManager.persist(new Message("First"));
			assertThat(Message.findAll(Message.class).size(), is(1));
			
			Message message = Message.createWithCustomTransactionMode(Message.class, entityManager);
			message.setMessage("Second");
			message.persist();
			assertThat(Message.findAll(Message.class).size(), is(2));

			throw new Exception("I want to rollack");
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			entityManager.close();
		}

		assertThat(Message.findAll(Message.class).size(), is(0));
	}

	@Test
	public void shouldBeCommittedAccrodingToExternalTransaction() {
		EntityManager entityManager = null;
		EntityTransaction transaction = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			
			entityManager.persist(new Message("First"));
			assertThat(Message.findAll(Message.class).size(), is(1));
			
			Message message = Message.createWithCustomTransactionMode(Message.class, entityManager);
			message.setMessage("Second");
			message.persist();
			assertThat(Message.findAll(Message.class).size(), is(2));
			
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			entityManager.close();
		}

		assertThat(Message.findAll(Message.class).size(), is(2));
	}
}
