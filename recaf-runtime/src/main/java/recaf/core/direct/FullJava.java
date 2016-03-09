package recaf.core.direct;

import recaf.core.ISupply;
import static recaf.core.EvalJavaHelper.toValue;

public interface FullJava<R> extends EvalJavaStmt<R, IEval>, EvalJavaExpr {  
	
	@SuppressWarnings("unchecked")
	@Override
	default <T> ISupply<T> Exp(IEval exp) {
		return () -> (T) toValue(exp.eval());
	}
	
	@Override
	default IExec ExpStat(IEval e) {
		return l -> { e.eval(); };
	}
}