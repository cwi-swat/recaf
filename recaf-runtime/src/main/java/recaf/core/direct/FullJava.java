package recaf.core.direct;

public interface FullJava<R> extends EvalJavaStmt<R, IEval>, EvalJavaExpr {  
	
	@SuppressWarnings("unchecked")
	@Override
	default <T> ISupply<T> Exp(IEval exp) {
		return () -> {
			try {
				return (T) exp.eval();
			}
			catch (Throwable e) {
				throw new RuntimeException(e);
			}
		};
	}
	
	@Override
	default IExec ExpStat(IEval e) {
		return l -> { e.eval(); };
	}
}