package recaf.tests.extensions;

import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestVirtualization extends BaseTest {
	
	@Test
	public void TestSimpleSwitch() throws CompiletimeException {
		compileAndRun("TestSwitch");
	}
}
