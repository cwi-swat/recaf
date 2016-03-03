package recaf.tests.extensions;

import org.junit.Test;

import recaf.tests.BaseTestPartial;
import recaf.tests.CompiletimeException;

public class TestUsingExtension extends BaseTestPartial {

	@Test
	public void TestUsingCompilation() throws CompiletimeException {
		compile("TestUsing");
	}
}
