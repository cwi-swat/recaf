package recaf.tests.virtualization;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTestFull;
import recaf.tests.CompiletimeException;

public class TestVirtualizationExpressions extends BaseTestFull {
	
	@Test
	public void TestMethodLookup1() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestExprMethodLookup1");

		Assert.assertEquals("4", output);
	}
	
	@Test
	public void TestMethodLookup2() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestExprMethodLookup2");

		Assert.assertEquals("4", output);
	}
	
	@Test
	public void TestMethodLookup3() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestExprMethodLookup3");

		Assert.assertEquals("4", output);
	}

}
