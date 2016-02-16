package recaf.core.generic;

import recaf.core.cps.CD;

public interface CaseCPS<R, V> extends App2<CaseCPS.t, R, V>, CD<R, V> {
	static class t { }

	static <A, B> CaseCPS<A, B> prj(App2<CaseCPS.t, A, B> app) { 
		return (CaseCPS<A, B>) app; 
	}
}
