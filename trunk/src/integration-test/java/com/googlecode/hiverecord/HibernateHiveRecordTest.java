package com.googlecode.hiverecord;

import org.junit.After;
import org.junit.Before;

import com.googlecode.hiverecord.support.AbstractHiveRecordTest;

public class HibernateHiveRecordTest extends AbstractHiveRecordTest {
	@Before
	public void readyHiveRecordSessionFactory() {
		EntitySessionFactory.register(createSessionFactory());
	}

	@After
	public void clearHiveRecordSessionFactory() {
		EntitySessionFactory.unregister();
	}
}