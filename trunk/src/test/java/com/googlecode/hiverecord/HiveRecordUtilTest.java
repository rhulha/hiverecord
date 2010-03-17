package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;

public class HiveRecordUtilTest {
	@Test
	public void sessionShouldBeClosedQueitlyEvenThoughItIsNull() {
		Session session = null;
		HiveRecordUtil.closeQuietly(session);
	}

	@Test
	public void sessionShouldBeClosed() {
		HiveRecordSessionFactory.register(createSessionFactory());
		
		Session session = HiveRecordSessionFactory.openSession();
		assertThat(session.isConnected(), is(true));
		HiveRecordUtil.closeQuietly(session);
		assertThat(session.isConnected(), is(false));
		
		HiveRecordSessionFactory.clear();
	}
	
	SessionFactory createSessionFactory() {
		try {
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg = (AnnotationConfiguration) cfg.configure();
			return cfg.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
}
