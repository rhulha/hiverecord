package com.googlecode.hiverecord;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

public class HibernateSessionManager<T> implements SessionManager<T> {
	private static Log LOG = LogFactory.getLog(HibernateSessionManager.class);
	
	SessionFactory sessionFactory;
	Session session;
	Transaction transaction;
	boolean rollbacked = false;
	
	public HibernateSessionManager(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public T find(Class<T> entityClass, Object primaryKey) {
		ready();
		beginTransaction();
		try {
			return (T) session.get(entityClass, (Serializable)primaryKey);
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
			return (List<T>) session.createCriteria(entityClass).list();
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
			return session.isOpen();
		} finally {
			closeQuietly();				
		}
	}

	@SuppressWarnings("unchecked")
	public T merge(T entity) {
		ready();
		beginTransaction();
		try {
			return (T)session.merge(entity);
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
			session.persist(entity);
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
			session.delete(entity);
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
		} catch (Exception e) {
			LOG.error(e.toString(), e);
		}
	}

	private void rollback(Exception e) {
		if (transaction != null) {
			transaction.rollback();
			LOG.info("Transaction rollback due to " + e.toString());
		}
	}
	
	public void closeQuietly() {
		if (session != null) {
			try {
				session.close();
			} catch (Exception e) {
				LOG.error(e.toString(), e);
			}
		}
	}
	
	private void ready() {
		try {
			session = sessionFactory.openSession();
		} catch (Exception e) {
			throw new HiveRecordException(e.toString(), e);
		}
	}

	private void beginTransaction() {
		try {
			transaction = session.beginTransaction();
			transaction.begin();
		} catch (Exception e) {
			session.close();
			throw new RuntimeException(e.toString(), e);
		}
	}
}
