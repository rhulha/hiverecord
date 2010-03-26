package com.googlecode.hiverecord;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public abstract class HiveRecord<T> {
	EntitySession entitySession;
	
	public void persist() {
		SessionManagerFactory.obtainSessionManager().persist(this);
	}

	public void remove() {
		SessionManagerFactory.obtainSessionManager().remove(this);
	}

	@SuppressWarnings("unchecked")
	public T merge() {
		SessionManager<T> sm= SessionManagerFactory.obtainSessionManager();
		return sm.merge((T) this);
	}
	
	public static <T> T find(Class<T> clazz, Serializable id) {
		SessionManager<T> sm= SessionManagerFactory.obtainSessionManager();
		return sm.find(clazz, id);
	}		

	public static <T> List<T> findAll(Class<T> clazz) {
		SessionManager<T> sm= SessionManagerFactory.obtainSessionManager();
		return sm.findAll(clazz);
	}


	public static <T> T createWithCustomTransactionMode(Class<T> clazz,
			EntitySession entitySession) {
		try {
			T result = clazz.newInstance();
			Field field = clazz.getSuperclass().getDeclaredField("entitySession");
			field.setAccessible(true);
			field.set(result, entitySession);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
