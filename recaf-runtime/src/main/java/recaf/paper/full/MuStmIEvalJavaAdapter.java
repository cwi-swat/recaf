package recaf.paper.full;

import java.util.function.Supplier;

import recaf.paper.expr.IEval;

public interface MuStmIEvalJavaAdapter<R,S> extends MuStmGenericJavaAdapter<R, S, IEval> {

	@Override
	default <T> Supplier<T> adapt(IEval e) {
		return () -> {
			try { 
				return (T) e.eval(); 
			}
			catch (Throwable t) {
				throw new RuntimeException(t);
			}
		};
	}

}
