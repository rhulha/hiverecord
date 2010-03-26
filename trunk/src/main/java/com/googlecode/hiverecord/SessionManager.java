package com.googlecode.hiverecord;

import java.util.List;

@Deprecated
public interface SessionManager<T> {
	void persist(T entity);
	T merge(T entity);
	void remove(T entity);
	T find(Class<T> entityClass, Object primaryKey);
	List<T> findAll(Class<T> entityClass);
	boolean isOpen();
}
