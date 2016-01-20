package recaf.tests.async;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class AsyncExtensionTests extends BaseTest {

	@Test
	public void SimpleAsyncNoAwaitTest() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("SimpleAsyncNoAwait");
		Assert.assertEquals("41", output);	
	}

	@Test
	public void SimpleAsyncWithAwaitTest() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("SimpleAsyncWithAwait");
		Assert.assertEquals("42", output);	
	}
}
