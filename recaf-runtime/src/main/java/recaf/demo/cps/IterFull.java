package recaf.demo.cps;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.CD;
import recaf.core.cps.SD;
import recaf.core.direct.IEval;
import recaf.core.expr.EvalJavaExpr;
import recaf.core.expr.EvalJavaHelper;
import recaf.core.full.JavaStmtAlgAdapter;
import static recaf.core.expr.EvalJavaHelper.toValue;

public interface IterFull<R> extends JavaStmtAlgAdapter<R, SD<R>, CD<R>>, JavaMethodAlg<Iterable<R>, SD<R>>, EvalJavaExpr {

	@Override
	default Iter<R> base() {
		return new Iter<R>();
	}
	
	default <U> SD<R> Yield(IEval exp) {
		return base().Yield(() -> (U)toValue(exp.eval()));
	}

	default <U> SD<R> YieldFrom(IEval exp) {
		return base().YieldFrom(() -> (Iterable<U>)toValue(exp.eval()));
	}

	@Override
	default Iterable<R> Method(SD<R> body) {
		Iterable<R> x = base().Method(body);
		return (Iterable<R>)toValue(x);
	}
}
