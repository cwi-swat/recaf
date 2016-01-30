package recaf.tests.extensions;

import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestParsingExtension extends BaseTest {

	@Test
	public void TestParsing() throws CompiletimeException {
		compile("TestParsing");
	}
}
