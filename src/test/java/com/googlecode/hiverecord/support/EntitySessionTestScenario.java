package com.googlecode.hiverecord.support;

public interface EntitySessionTestScenario {
	abstract void shouldBeCreatedWithSession();

	abstract void shouldBePersisted();

	abstract void shouldBeMerged();

	abstract void shouldBeRemoved();

	abstract void shouldBeFound();

	abstract void allEntitiesShouldBeFound();

	//TODO if an exception occurs when closing it?
	abstract void canBeClosedSafely();

	abstract void transactionShouldBeBegun();
	
	abstract void transactionCanBeRollbacked();

	abstract void transactionCanBeCommitted();
}