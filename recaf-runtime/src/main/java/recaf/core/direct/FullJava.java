package recaf.core.direct;

import java.util.function.Supplier;

public interface FullJava<R> extends EvalJavaStmt<R, IEvalEx<?>>, EvalJavaExpr {  
	
	@SuppressWarnings("unchecked")
	@Override
	default <T> Supplier<T> Exp(IEvalEx<?> exp) {
		return () -> (T) exp.eval();
	}
	
	@Override
	default IExecEx<?> ExpStat(IEvalEx<?> e) {
		return l -> { e.eval(); };
	}
}