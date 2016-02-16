package recaf.core.generic;

import higher.App;
import recaf.core.functional.ED;

public interface ExpCPS<T> extends App<ExpCPS.t, T>, ED<T> {
	static class t { }

	static <A> ExpCPS<A> prj(App<ExpCPS.t, A> app) { 
		return (ExpCPS<A>) app; 
	}
	
}
