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
	
	@Test
	public void TestStream_with_awaitFor() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestStream_with_awaitFor");
		Assert.assertEquals("1\n" + 
				"42\n" + 
				"42\n" + 
				"42\n" + 
				"42\n" + 
				"42", output);	
	}
	
	@Test
	public void TestStream_complex() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestStream_Complex");
		Assert.assertEquals("0\n" + 
				"1\n" + 
				"3\n" + 
				"6\n" + 
				"10\n" + 
				"15", output);	
	}
}
