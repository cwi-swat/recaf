package recaf.tests.extensions;

import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestResetExtension extends BaseTest {

	@Test
	public void TestResetCompilation() throws CompiletimeException {
		compile("TestReset");
	}
}
