package recaf.tests.extensions;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTestPartial;
import recaf.tests.CompiletimeException;

public class TestBacktrackingExtension extends BaseTestPartial {
	@Test
	public void TestBacktracking() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestBacktracking");
		Assert.assertEquals("[(2, 4)]", output);	
	}
}
