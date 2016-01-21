package recaf.tests.iter;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class IterExtensionTests extends BaseTest {

	@Test
	public void TestIterStreamLibrary() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestPullStreams");
		Assert.assertEquals("22", output);	
	}
}
