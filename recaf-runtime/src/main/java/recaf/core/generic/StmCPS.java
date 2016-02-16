package recaf.core.generic;

import higher.App;
import recaf.core.functional.SD;

public interface StmCPS<R> extends App<StmCPS.t, R>, SD<R> {
	static class t { }

	static <A> StmCPS<A> prj(App<StmCPS.t, A> app) { 
		return (StmCPS<A>) app; 
	}
}
