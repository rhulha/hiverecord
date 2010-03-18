package com.googlecode.hiverecord;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.googlecode.hiverecord.support.Message;

public class SimpleTest {
	@Test
	public void test() {
		EntityManager em = Persistence.createEntityManagerFactory("hiverecord").createEntityManager();
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Message message = new Message("Hello World");
		em.persist(message);
		tx.commit();

		tx = em.getTransaction();
		tx.begin();
		em.remove(message);
		tx.commit();
		em.close();
	}
}
