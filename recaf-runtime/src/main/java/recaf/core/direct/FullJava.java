package recaf.core.direct;

import java.util.function.Supplier;

public interface FullJava<R> extends EvalJavaStmt<R, IEval>, EvalJavaExpr {  
	@SuppressWarnings("unchecked")
	@Override
	default <T> Supplier<T> Exp(IEval exp) {
		return () -> (T)exp.eval();
	}
	
	@Override
	default IExec ExpStat(IEval e) {
		return l -> { e.eval(); };
	}
}