package com.googlecode.hiverecord;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JPASessionManager<T> implements SessionManager<T> {
	private static Log LOG = LogFactory.getLog(JPASessionManager.class);
	
	EntityManagerFactory factory;
	EntityManager manager;
	EntityTransaction transaction;
	boolean rollbacked = false;

	public JPASessionManager(EntityManagerFactory entityManagerFactory) {
		this.factory = entityManagerFactory;
	}

	public T find(Class<T> entityClass, Object primaryKey) {
		ready();
		beginTransaction();
		try {
			return (T) manager.find(entityClass, primaryKey);
		} catch(Exception e) {
			rollback(e);
			throw new HiveRecordException(e.toString(), e);
		} finally {
			commit();
			closeQuietly();			
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> entityClass) {
		ready();
		beginTransaction();
		try {
			return (List<T>) manager.createQuery("SELECT o FROM " + entityClass.getName() + " o").getResultList();
		} catch(Exception e) {
			rollback(e);
			throw new HiveRecordException(e.toString(), e);
		} finally {
			commit();
			closeQuietly();			
		}
	}

	public boolean isOpen() {
		ready();
		try {
			return manager.isOpen();
		} finally {
			closeQuietly();				
		}
	}

	public T merge(T entity) {
		ready();
		beginTransaction();
		try {
			return (T)manager.merge(entity);
		} catch(Exception e) {
			rollback(e);
			throw new HiveRecordException(e.toString(), e);
		} finally {
			commit();
			closeQuietly();			
		}
	}

	public void persist(T entity) {
		ready();
		beginTransaction();
		try {
			manager.persist(entity);
		} catch(Exception e) {
			rollback(e);
			throw new HiveRecordException(e.toString(), e);
		} finally {
			commit();
			closeQuietly();			
		}		
	}

	public void remove(T entity) {
		ready();
		beginTransaction();
		try {
			manager.remove(manager.merge(entity));
		} catch(Exception e) {
			rollback(e);
			throw new HiveRecordException(e.toString(), e);
		} finally {
			commit();
			closeQuietly();			
		}		
	}

	private void commit() {
		if (rollbacked) {
			return;
		}
		
		try {
			transaction.commit();
			LOG.info("Transaction has just commited.");
		} catch (Exception e) {
			LOG.error(e.toString(), e);
		}
	}

	private void rollback(Exception e) {
		rollbacked = true;
		
		if (transaction != null) {
			transaction.rollback();
			LOG.info("Transaction has just rollbacked due to " + e.toString());
		}
	}
	
	public void closeQuietly() {
		if (manager != null) {
			try {
				manager.close();
			} catch (Exception e) {
				LOG.error(e.toString(), e);
			}
		}
	}
	
	private void ready() {
		try {
			manager = factory.createEntityManager();
		} catch (Exception e) {
			throw new HiveRecordException(e.toString(), e);
		}
	}

	private void beginTransaction() {
		try {
			LOG.info("Transaction has just began.");
			transaction = manager.getTransaction();
			transaction.begin();
		} catch (Exception e) {
			manager.close();
			throw new RuntimeException(e.toString(), e);
		}
	}
}
