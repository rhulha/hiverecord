package com.googlecode.hiverecord;

import org.junit.After;
import org.junit.Before;


public class HibernateHiveRecordIngegrationTest extends AbstractHiveRecordTest {
	@Before
	public void readyHiveRecordSessionFactory() {
		SessionManagerFactory.register(createSessionFactory());		
	}

	@After
	public void clearHiveRecordSessionFactory() {
		SessionManagerFactory.unregister();
	}
}
