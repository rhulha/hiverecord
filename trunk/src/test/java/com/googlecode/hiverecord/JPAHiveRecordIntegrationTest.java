package com.googlecode.hiverecord;

import org.junit.After;
import org.junit.Before;


public class JPAHiveRecordIntegrationTest extends AbstractHiveRecordTest {
	@Before
	public void readyHiveRecordSessionFactory() {
		SessionManagerFactory.register(createEntityManagerFactory());		
	}

	@After
	public void clearHiveRecordSessionFactory() {
		SessionManagerFactory.unregister();
	}
}
