package recaf.tests.extensions;

import org.junit.Assert;
import org.junit.Test;

import recaf.tests.BaseTest;
import recaf.tests.CompiletimeException;

public class TestSolveExtension extends BaseTest {

	@Test
	public void TestSolve() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestSolve");
		Assert.assertEquals("{x=2, y=0}\n" + 
				"{x=3, y=0}\n" + 
				"{x=3, y=1}\n" + 
				"{x=4, y=0}", output);	
	}
	
	@Test
	public void TestSolve_alldifferent() throws CompiletimeException, RuntimeException {
		String output = compileAndRun("TestSolve_alldifferent");
		Assert.assertEquals("{w=0, x=-1, y=2, z=5}\n" + 
				"{w=0, x=-1, y=2, z=6}\n" + 
				"{w=0, x=-1, y=2, z=7}\n" + 
				"{w=0, x=-1, y=3, z=5}\n" + 
				"{w=0, x=-1, y=3, z=6}\n" + 
				"{w=0, x=-1, y=3, z=7}\n" + 
				"{w=0, x=-1, y=4, z=5}\n" + 
				"{w=0, x=-1, y=4, z=6}\n" + 
				"{w=0, x=-1, y=4, z=7}\n" + 
				"{w=0, x=1, y=2, z=5}\n" + 
				"{w=0, x=1, y=2, z=6}\n" + 
				"{w=0, x=1, y=2, z=7}\n" + 
				"{w=0, x=1, y=3, z=5}\n" + 
				"{w=0, x=1, y=3, z=6}\n" + 
				"{w=0, x=1, y=3, z=7}\n" + 
				"{w=0, x=1, y=4, z=5}\n" + 
				"{w=0, x=1, y=4, z=6}\n" + 
				"{w=0, x=1, y=4, z=7}\n" + 
				"{w=0, x=2, y=3, z=5}\n" + 
				"{w=0, x=2, y=3, z=6}\n" + 
				"{w=0, x=2, y=3, z=7}\n" + 
				"{w=0, x=2, y=4, z=5}\n" + 
				"{w=0, x=2, y=4, z=6}\n" + 
				"{w=0, x=2, y=4, z=7}\n" + 
				"{w=1, x=-1, y=2, z=5}\n" + 
				"{w=1, x=-1, y=2, z=6}\n" + 
				"{w=1, x=-1, y=2, z=7}\n" + 
				"{w=1, x=-1, y=3, z=5}\n" + 
				"{w=1, x=-1, y=3, z=6}\n" + 
				"{w=1, x=-1, y=3, z=7}\n" + 
				"{w=1, x=-1, y=4, z=5}\n" + 
				"{w=1, x=-1, y=4, z=6}\n" + 
				"{w=1, x=-1, y=4, z=7}\n" + 
				"{w=1, x=0, y=2, z=5}\n" + 
				"{w=1, x=0, y=2, z=6}\n" + 
				"{w=1, x=0, y=2, z=7}\n" + 
				"{w=1, x=0, y=3, z=5}\n" + 
				"{w=1, x=0, y=3, z=6}\n" + 
				"{w=1, x=0, y=3, z=7}\n" + 
				"{w=1, x=0, y=4, z=5}\n" + 
				"{w=1, x=0, y=4, z=6}\n" + 
				"{w=1, x=0, y=4, z=7}\n" + 
				"{w=1, x=2, y=3, z=5}\n" + 
				"{w=1, x=2, y=3, z=6}\n" + 
				"{w=1, x=2, y=3, z=7}\n" + 
				"{w=1, x=2, y=4, z=5}\n" + 
				"{w=1, x=2, y=4, z=6}\n" + 
				"{w=1, x=2, y=4, z=7}", output);	
	}
}
