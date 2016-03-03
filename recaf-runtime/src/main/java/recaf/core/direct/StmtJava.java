package recaf.core.direct;

import recaf.core.ISupply;

public interface StmtJava<R> extends EvalJavaStmt<R, ISupply<?>> {
	@SuppressWarnings("unchecked")
	@Override
	default <T> ISupply<T> Exp(ISupply<?> e) {
		return (ISupply<T>) e;
	}
	
	@Override
	default IExec ExpStat(ISupply<?> thunk) {
		return l -> { thunk.get(); };
	}
}
