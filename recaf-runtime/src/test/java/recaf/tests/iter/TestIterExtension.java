package recaf.tests.iter;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestIterExtension extends BaseTest {

	@Test
	public void TestIterStreamLibrary() throws CompiletimeException, RuntimeException {
		compile("PStream");

		String output = compileAndRun("TestPullStreams");
		Assert.assertEquals("22", output);	
	}
	
	@Test
	public void TestFibIter() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestFibIter");
		Assert.assertEquals("55", output);	
	}
}
