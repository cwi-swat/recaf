package recaf.core.cps;

import recaf.core.ISupply;
import recaf.core.alg.JavaStmtAlg;
import recaf.core.direct.EvalJavaExpr;
import recaf.core.direct.IEval;

public interface FullJava<R> extends JavaStmtAlg<R, IEval, SD<R>, CD<R>>, EvalJavaExpr {
	
	@SuppressWarnings("unchecked")
	@Override
	default <T> ISupply<T> Exp(IEval exp) {
		return () -> (T)exp.eval();
	}
	
	@Override
	default SD<R> ExpStat(IEval thunk) {
		return (label, rho, sigma, brk, contin, err) -> {
			try {
				thunk.eval();
			} catch (Throwable t) {
				err.accept(t);
				return;
			}
			sigma.call();
		};
	}
	
}