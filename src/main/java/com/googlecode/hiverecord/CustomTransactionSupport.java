package com.googlecode.hiverecord;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.hibernate.Session;

public class CustomTransactionSupport<T> {

	protected EntitySession customEntitySession;

	public static <T> T createWithCustomTransactionMode(Class<T> clazz,
			EntitySession entitySession) {
		try {
			T result = clazz.newInstance();
			Field field = clazz.getSuperclass().getSuperclass()
					.getDeclaredField("customEntitySession");
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

	public CustomTransactionSupport() {
		super();
	}

	protected boolean customTransactionMode() {
		return customEntitySession != null;
	}

}