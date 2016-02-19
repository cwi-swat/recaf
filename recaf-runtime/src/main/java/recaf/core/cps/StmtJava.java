package recaf.core.cps;

import java.util.function.Supplier;

public interface StmtJava<R> extends EvalJavaStmt<R, Supplier<?>> {
	@SuppressWarnings("unchecked")
	@Override
	default <T> Supplier<T> Exp(Supplier<?> e) {
		return (Supplier<T>) e;
	}
	
	@Override
	default SD<R> ExpStat(Supplier<?> thunk) {
		return (label, rho, sigma, brk, contin, err) -> {
			try {
				thunk.get();
			} catch (Throwable t) {
				err.accept(t);
				return;
			}
			sigma.call();
		};
	}
}
