package recaf.core.cps;

import java.util.function.Supplier;

public interface StmtJava<R> extends EvalJavaStmt<R, Supplier<?>> {
	@SuppressWarnings("unchecked")
	@Override
	default <T> Supplier<T> Exp(Supplier<?> e) {
		return (Supplier<T>) e;
	}
}
