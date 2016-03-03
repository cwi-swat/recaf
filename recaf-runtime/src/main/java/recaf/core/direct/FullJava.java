package recaf.core.direct;

import recaf.core.ISupply;

public interface FullJava<R> extends EvalJavaStmt<R, IEval>, EvalJavaExpr {  
	
	@SuppressWarnings("unchecked")
	@Override
	default <T> ISupply<T> Exp(IEval exp) {
		return () -> (T)exp.eval();
	}
	
	@Override
	default IExec ExpStat(IEval e) {
		return l -> { e.eval(); };
	}
}