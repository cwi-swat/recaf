package recaf.tests.extensions;

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
	
	@Test
	public void TestIter_YieldEachRec() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestIter_YieldEachRec");
		Assert.assertEquals("i = 10\n" + 
				"i = 9\n" + 
				"i = 8\n" + 
				"i = 7\n" + 
				"i = 6\n" + 
				"i = 5\n" + 
				"i = 4\n" + 
				"i = 3\n" + 
				"i = 2\n" + 
				"i = 1", output);	
	}
}
