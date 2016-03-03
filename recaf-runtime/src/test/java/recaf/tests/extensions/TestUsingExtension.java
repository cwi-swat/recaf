package recaf.tests.extensions;

import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestUsingExtension extends BaseTest {

	@Test
	public void TestUsingCompilation() throws CompiletimeException {
		compile("TestUsing");
	}
}
