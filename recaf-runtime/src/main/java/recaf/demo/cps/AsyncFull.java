package recaf.demo.cps;

import java.util.concurrent.CompletableFuture;

import java.util.concurrent.Future;
import java.util.function.Function;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.CD;
import recaf.core.cps.SD;
import recaf.core.direct.IEval;
import recaf.core.expr.EvalJavaExpr;
import recaf.core.full.JavaStmtAlgAdapter;

import static recaf.core.expr.EvalJavaHelper.toValue;

public interface AsyncFull<R> extends JavaStmtAlgAdapter<R, SD<R>, CD<R>>, JavaMethodAlg<Future<R>, SD<R>>, EvalJavaExpr {
	
	@Override
	default Async<R> base() {
		return new Async<R>() {};
	}
	
	// Should be in Await interface (?)
	default <T> SD<R> Await(IEval e, Function<T, SD<R>> body) {
		return base().Await(() -> (CompletableFuture<T>)toValue(e.eval()), body);
	}
	
	@Override
	default Future<R> Method(SD<R> body) {
		return base().Method(body);
	}

}
