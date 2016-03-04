package recaf.tests.virtualization;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestVirtualizationExpressions extends BaseTest {
	
	@Test
	public void TestPrivateConstructor() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestExprPrivateConstructor");

		Assert.assertEquals("AA(0)", output);
	}
	
	@Test
	public void TestNoConstructor() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestExprNoConstructor");

		Assert.assertEquals("AB(0)", output);
	}
	
	@Test
	public void TestSimpleMethodLookup() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestExprSimpleMethodLookup");
		Assert.assertEquals("4", output);
	}
	
	@Test
	public void TestInheritanceMethodLookup() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestExprInheritanceMethodLookup");
		Assert.assertEquals("-1", output);
	}
	

	@Test
	public void TestPlus() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestExprPlus");
		Assert.assertEquals("2\n2.0\n2.0\n2.0\n11\n11\n11.0\n1.01\n1.01\n11.0\n2.0\n2.0\n2.0\n2.0\n2.0\n11", output);
	}
	
	@Test
	public void TestInvokeSuper() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestExprInvokeSuper");
		Assert.assertEquals("son(BaseTestExprInvokeSuper)", output);
	}

}
