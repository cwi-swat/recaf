package recaf.tests.virtualization;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestVirtualization extends BaseTest {

	@Test
	public void TestSwitch_break_default_beginning() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestSwitch_break_default_beginning");

		Assert.assertEquals("two", output);
	}

	@Test
	public void TestSwitch_break_first() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestSwitch_break_first");

		Assert.assertEquals("one", output);
	}

	@Test
	public void TestSwitch_break_second_withdefault() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestSwitch_break_second_withdefault");

		Assert.assertEquals("two\n" + "with default", output);
	}

	@Test
	public void TestSwitch_break_second() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestSwitch_break_second");

		Assert.assertEquals("one\n" + "two", output);
	}

	@Test
	public void TestSwitch_nested_default_beginning() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestSwitch_nested_default_beginning");

		Assert.assertEquals("three\n" + "two", output);
	}

	@Test
	public void TestFor_noBreak() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestFor_noBreak");

		Assert.assertEquals("15", output);
	}

	@Test
	public void TestFor_ordinary() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestFor_ordinary");

		Assert.assertEquals("45", output);
	}

	@Test
	public void TestFor_withBreak() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestFor_withBreak");

		Assert.assertEquals("10", output);
	}

	@Test
	public void TestWhile_no_break() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestWhile_no_break");

		Assert.assertEquals("15", output);
	}

	@Test
	public void TestWhile_withBreak() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestWhile_withBreak");

		Assert.assertEquals("6", output);
	}
}
