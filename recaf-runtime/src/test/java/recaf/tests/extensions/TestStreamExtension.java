package recaf.tests.extensions;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestStreamExtension extends BaseTest {

	@Test
	public void TestStream_with_yield() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestStream_with_yield");
		Assert.assertEquals("1\n" + 
				"2\n" + 
				"3\n" + 
				"4\n" + 
				"5", output);	
	}
	
	@Test
	public void TestStream_with_yieldFrom() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestStream_with_yieldFrom");
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
	
	@Test
	public void TestStream_with_await() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestStream_with_await");
		Assert.assertEquals("1\n" + 
				"2\n" + 
				"42\n" + 
				"4\n" + 
				"5", output);	
	}
}
