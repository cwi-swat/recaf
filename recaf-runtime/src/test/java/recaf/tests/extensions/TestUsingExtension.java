package recaf.tests.extensions;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestUsingExtension extends BaseTest {

	@Test
	public void TestUsing() throws CompiletimeException {
		String output = compileAndRun("TestUsing");
		Assert.assertEquals("test", output);	
	}
}
