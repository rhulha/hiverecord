package com.googlecode.hiverecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.googlecode.hiverecord.support.Pizza;


public class EntitySessionTest {
	EntitySession O = new EntitySession();
	
	@Test
	public void tablaNameShouldBeObtainedFromEntity() {
		assertThat(O.tableName(Message.class), is("Message"));
		assertThat(O.tableName(Pizza.class), is("OhPizza"));
	}
}
