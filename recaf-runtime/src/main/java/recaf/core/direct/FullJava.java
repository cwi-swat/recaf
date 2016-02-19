package recaf.core.direct;

import java.util.function.Supplier;

public interface FullJava<R> extends EvalJavaStmt<R, IEval>, EvalJavaExpr {  
	@SuppressWarnings("unchecked")
	default <T> Supplier<T> Exp(IEval exp) {
		return () -> (T)exp.eval();
	}
	
}