package recaf.tests.async;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestAsyncExtension extends BaseTest {

	@Test
	public void TestAsyncNoAwait() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestAsyncNoAwait");
		Assert.assertEquals("41", output);	
	}

	@Test
	public void TestAsyncWithAwait() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestAsyncWithAwait");
		Assert.assertEquals("42", output);	
	}
	
	@Test
	public void TestAsyncComplex() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestAsyncComplex");
		Assert.assertEquals("main\n"
							+ "delayed op\n"
							+ "delayed op\n"
							+ "84", output);	
	}
	
	@Test
	public void TestFibRecAsync() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestFibRecAsync");
		Assert.assertEquals("55", output);	
	}
}
