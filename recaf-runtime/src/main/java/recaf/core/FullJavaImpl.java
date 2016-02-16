package recaf.core;

import java.util.function.Supplier;

import recaf.core.direct.EvalJavaStmt;
import recaf.core.direct.IEval;
import recaf.core.direct.JavaExprEvaluator;

public class FullJavaImpl<R> implements EvalJavaStmt<IEval>, JavaExprEvaluator{  
	@SuppressWarnings("unchecked")
	public <T> Supplier<T> Exp(IEval exp) {
		return () -> (T)exp.eval();
	}
}