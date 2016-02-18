package recaf.core.direct;

import java.util.function.Supplier;

public interface FullJava<R> extends EvalJavaStmt<IEval>, EvalJavaExpr {  
	@SuppressWarnings("unchecked")
	default <T> Supplier<T> Exp(IEval exp) {
		return () -> (T)exp.eval();
	}
	
	@Override
	default IExec ExpStat(IEval exp) {
		return l -> { exp.eval(); };
	}
}