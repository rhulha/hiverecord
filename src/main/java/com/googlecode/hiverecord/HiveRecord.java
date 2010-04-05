package com.googlecode.hiverecord;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

public abstract class HiveRecord<T> {
	EntitySession customEntitySession;

	public void persist() {
		if (customTransactionMode()) {
			customEntitySession.persist(this);
			return;
		}

		EntitySession entitySession = EntitySessionFactory
				.obtainEntitySession();
		entitySession.beginTransaction();

		try {
			entitySession.persist(this);
			entitySession.commit();
		} catch (Exception e) {
			entitySession.rollback();
			throw new HiveRecordException(e.toString(), e);
		} finally {
			entitySession.close();
		}
	}

	public void remove() {
		if (customTransactionMode()) {
			customEntitySession.remove(this);
			return;
		}

		EntitySession entitySession = EntitySessionFactory
				.obtainEntitySession();
		entitySession.beginTransaction();

		try {
			entitySession.remove(entitySession.merge(this));
			entitySession.commit();
		} catch (Exception e) {
			entitySession.rollback();
			throw new HiveRecordException(e.toString(), e);
		} finally {
			entitySession.close();
		}
	}

	@SuppressWarnings("unchecked")
	public T merge() {
		if (customTransactionMode()) {
			return (T) customEntitySession.merge(this);
		}

		EntitySession entitySession = EntitySessionFactory
				.obtainEntitySession();
		entitySession.beginTransaction();

		try {
			T result = (T) entitySession.merge(this);
			entitySession.commit();
			return result;
		} catch (Exception e) {
			entitySession.rollback();
			throw new HiveRecordException(e.toString(), e);
		} finally {
			entitySession.close();
		}
	}

	public static <T> Long count(Class<T> clazz) {
		EntitySession entitySession = EntitySessionFactory
				.obtainEntitySession();
		return entitySession.count(clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> top(Class<T> classz, int topCount, Order order) {
		EntitySession entitySession = EntitySessionFactory
		.obtainEntitySession();
		return (List<T>)entitySession.top(classz, topCount, order);
	}

	@SuppressWarnings("unchecked")
	public static <T> T find(Class<T> clazz, Serializable id) {
		EntitySession entitySession = EntitySessionFactory
				.obtainEntitySession();
		return (T) entitySession.find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public static <T> T find(Class<T> clazz, Serializable id,
			EntitySession entitySession) {
		return (T) entitySession.find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> clazz) {
		EntitySession entitySession = EntitySessionFactory
				.obtainEntitySession();
		return (List<T>) entitySession.findAll(clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> findAll(Class<T> clazz,
			EntitySession entitySession) {
		return (List<T>) entitySession.findAll(clazz);
	}

	public static <T> T createWithCustomTransactionMode(Class<T> clazz,
			EntitySession entitySession) {
		try {
			T result = clazz.newInstance();
			Field field = clazz.getSuperclass().getDeclaredField(
					"customEntitySession");
			field.setAccessible(true);
			field.set(result, entitySession);
			return result;
		} catch (Exception e) {
			throw new HiveRecordException(e.toString(), e);
		}
	}

	public static <T> T createWithCustomTransactionMode(Class<T> clazz,
			EntityManager entityManager) {
		return createWithCustomTransactionMode(clazz, new EntitySession(
				entityManager));
	}

	public static <T> T createWithCustomTransactionMode(Class<T> clazz,
			Session session) {
		return createWithCustomTransactionMode(clazz,
				new EntitySession(session));
	}

	private boolean customTransactionMode() {
		return customEntitySession != null;
	}
}
