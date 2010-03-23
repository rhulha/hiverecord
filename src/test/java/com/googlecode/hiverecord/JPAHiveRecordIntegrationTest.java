package com.googlecode.hiverecord;

import org.junit.After;
import org.junit.Before;

import com.googlecode.hiverecord.support.AbstractHiveRecordTest;

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
