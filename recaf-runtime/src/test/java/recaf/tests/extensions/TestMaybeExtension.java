package recaf.tests.extensions;

import org.junit.Test;

import recaf.tests.BaseTestPartial;
import recaf.tests.CompiletimeException;

public class TestMaybeExtension extends BaseTestPartial {

	@Test
	public void TestMaybe() throws CompiletimeException {
		compile("TestMaybe");
	}
}
