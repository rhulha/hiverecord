package com.googlecode.hiverecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HiveRecordUtil {
	private static final Log LOG = LogFactory.getLog(HiveRecordUtil.class);

	public static void closeQuietly(Session session) {
		if (session != null) {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}
	
	public static void fail(Transaction transaction, Exception e) {
		if (transaction != null) {
			transaction.rollback();
			LOG.info("Transaction rollback due to " + e.toString());
		}
	}
}
