package recaf.paper.full;

import recaf.paper.expr.IEval;

public interface MuStmIEvalJavaAdapter<R,S> extends MuStmGenericJavaAdapter<R, S, IEval> {

	@Override
	default <T> T adapt(IEval e) {
		try { return (T) e.eval(); } 
		catch (Throwable t) {throw new RuntimeException(t);}
	}

}
