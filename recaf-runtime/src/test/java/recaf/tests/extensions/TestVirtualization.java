package recaf.tests.extensions;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestVirtualization extends BaseTest {
	
	@Test
	public void TestSwitch() throws CompiletimeException {
		String output = compileAndRun("TestSwitch");
		
		Assert.assertEquals("one\n" + 
				"two\n" + 
				"default\n" + 
				"\n" + 
				"one\n" + 
				"\n" + 
				"one\n" + 
				"two\n" + 
				"\n" + 
				"two\n" + 
				"with default\n" + 
				"\n" + 
				"two\n" + 
				"\n" + 
				"three\n" + 
				"two", output);	
	}
}
