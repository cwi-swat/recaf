package recaf.tests.extensions;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestMaybeExtension extends BaseTest {

	@Test
	public void TestMaybe() throws CompiletimeException {
		String output = compileAndRun("TestMaybe");
		Assert.assertEquals("maybe\n" + 
				"Yes\n" + 
				"43", output);	
	}
}
