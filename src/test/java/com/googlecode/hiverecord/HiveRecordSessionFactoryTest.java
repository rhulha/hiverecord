package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.Test;

import com.googlecode.hiverecord.support.AbstractHiveRecordTest;

public class HiveRecordSessionFactoryTest extends AbstractHiveRecordTest {
	@Test(expected=IllegalStateException.class)
	public void sessionFactoryShouldBeRegistedBeforeUsingActiveRecord() {
		HiveRecordSessionFactory.openSession();
	}	
	
	@Test
	public void sessionShouldBeValidAfterRegisteringSessionFactory() {
		HiveRecordSessionFactory.register(createSessionFactory());
		
		Session session = HiveRecordSessionFactory.openSession();
		
		assertThat(session, is(notNullValue()));
		assertThat(session.isConnected(), is(true));
		
		HiveRecordSessionFactory.clear();
	}
}
