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
	
	@Test
	public void TestIterFib() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestIterFib");
		Assert.assertEquals("0\n" + 
				"1\n" + 
				"1\n" + 
				"2\n" + 
				"3\n" + 
				"5\n" + 
				"8\n" + 
				"13\n" + 
				"21\n" + 
				"34\n" + 
				"55\n" + 
				"89", output);	
	}
}
