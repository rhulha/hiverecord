package com.googlecode.hiverecord;

import org.junit.After;
import org.junit.Before;

import com.googlecode.hiverecord.support.AbstractHiveRecordTest;

public class JPAHiveRecordTest extends AbstractHiveRecordTest {
	@Before
	public void readyHiveRecordSessionFactory() {
		EntitySessionFactory.register(createEntityManagerFactory());
	}

	@After
	public void clearHiveRecordSessionFactory() {
		EntitySessionFactory.unregister();
	}
}
