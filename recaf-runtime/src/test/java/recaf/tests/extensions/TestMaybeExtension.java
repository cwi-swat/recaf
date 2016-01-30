package recaf.tests.extensions;

import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestMaybeExtension extends BaseTest {

	@Test
	public void TestMaybe() throws CompiletimeException {
		compile("TestMaybe");
	}
}
