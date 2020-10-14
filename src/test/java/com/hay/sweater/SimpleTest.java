package com.hay.sweater;

import org.junit.Test;
import org.junit.Assert;

class SimpleTest {

	@Test
	void test() {
		int x = 2;
		int y = 23;
		Assert.assertEquals(46, x * y);
	}
	
	@Test(expected = ArithmeticException.class )
	public void error() {
		int x = 0;
		int y = 1/x;
	}

}
