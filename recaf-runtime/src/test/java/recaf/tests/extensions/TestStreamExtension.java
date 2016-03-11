package recaf.tests.extensions;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestStreamExtension extends BaseTest {

	@Test
	public void TestSimpleStream() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestSimpleStream");
		Assert.assertEquals("1\n" + 
				"2\n" + 
				"3\n" + 
				"4\n" + 
				"5", output);	
	}
	
	@Test
	public void TestSimpleStream_with_yieldFrom() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestSimpleStream_with_yieldFrom");
		Assert.assertEquals("1\n" + 
				"2\n" + 
				"3\n" + 
				"11\n" + 
				"22\n" + 
				"33\n" + 
				"44\n" + 
				"55\n" + 
				"4\n" + 
				"5", output);	
	}
}
