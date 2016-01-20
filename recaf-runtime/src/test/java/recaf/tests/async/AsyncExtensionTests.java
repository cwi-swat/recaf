package recaf.tests.async;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class AsyncExtensionTests extends BaseTest {

	@Test
	public void SimpleAsyncNoAwaitTest() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestAsync");
		Assert.assertEquals("41", output);	
	}

	@Test
	public void SimpleAsyncWithAwaitTest() {
		assert(false);
	}
}
