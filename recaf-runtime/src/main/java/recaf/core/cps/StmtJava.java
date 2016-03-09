package recaf.core.cps;

import recaf.core.ISupply;
import recaf.core.alg.JavaStmtAlg;

public interface StmtJava<R> extends JavaStmtAlg<R, ISupply<?>, SD<R>, CD<R>> {
	
	@SuppressWarnings("unchecked")
	@Override
	default <T> ISupply<T> Exp(ISupply<?> exp) {
		return (ISupply<T>) exp;
	}
	
	@Override
	default SD<R> ExpStat(ISupply<?> thunk) {
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
