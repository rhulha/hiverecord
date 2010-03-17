package com.googlecode.hiverecord;

import static com.googlecode.hiverecord.HiveRecordUtil.*;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class HiveRecord<T> {
	//private static final Log LOG = LogFactory.getLog(HiveRecord.class);

	public Serializable save() {
		Session session = null;
		Transaction tx = null;
		try {
			session = HiveRecordSessionFactory.openSession();
			tx = session.beginTransaction();
			Serializable result = session.save(this);
			tx.commit();
			return result;
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			fail(tx, e);
			throw new HiveRecordException(e.toString(), e);					
		} finally {
			closeQuietly(session);			
		}		
	}

	@SuppressWarnings("unchecked")
	public T merge() {
		Session session = null;
		Transaction tx = null;
		try {
			session = HiveRecordSessionFactory.openSession();
			tx = session.beginTransaction();
			T result = (T)session.merge(this);
			tx.commit();
			return result;
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			fail(tx, e);
			throw new HiveRecordException(e.toString(), e);					
		} finally {
			closeQuietly(session);			
		}		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T find(Class<T> clazz, Serializable id) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HiveRecordSessionFactory.openSession();
			tx = session.beginTransaction();
			T result = (T) session.get(clazz, id);
			tx.commit();
			return result;
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			fail(tx, e);
			throw new HiveRecordException(e.toString(), e);					
		} finally {
			closeQuietly(session);			
		}
	}		

	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> clazz) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HiveRecordSessionFactory.openSession();
			tx = session.beginTransaction();
			List<T> result = (List<T>) session.createCriteria(clazz).list();
			tx.commit();
			return result;
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			fail(tx, e);
			throw new HiveRecordException(e.toString(), e);					
		} finally {
			closeQuietly(session);			
		}
	}		
}
