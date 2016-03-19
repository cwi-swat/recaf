package recaf.paper.full;

import java.util.function.Supplier;

import recaf.paper.expr.IEval;

public
//BEGIN_IEVAL_ADAPTER
interface MuStmIEvalJavaAdapter<R, S>
	extends MuStmJavaAdapter<R, S, IEval> {

	default <T> Supplier<T> adapt(IEval e) {
		return () -> { return (T) e.eval(); };
	}
}
//END_IEVAL_ADAPTER
