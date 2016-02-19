package recaf.core.direct;

import java.util.function.Supplier;

public interface StmtJava<R> extends EvalJavaStmt<R, Supplier<?>> {
	@SuppressWarnings("unchecked")
	@Override
	default <T> Supplier<T> Exp(Supplier<?> e) {
		return (Supplier<T>) e;
	}
	
	@Override
	default IExec ExpStat(Supplier<?> thunk) {
		return l -> { thunk.get(); };
	}
}
