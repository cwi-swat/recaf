package recaf.core.full;

import recaf.core.Ref;
import recaf.core.alg.JavaMethodAlg;
import recaf.core.alg.JavaStmtOnlyAlg;
import recaf.core.cps.CD;
import recaf.core.cps.EvalJavaStmt;
import recaf.core.cps.SD;
import recaf.core.expr.EvalJavaExpr;

public interface FullJavaCPS<R> extends JavaStmtAlgAdapter<R, SD<R>, CD<R>>, EvalJavaExpr, JavaMethodAlg<R, SD<R>> {

	@Override
	default JavaStmtOnlyAlg<R, SD<R>, CD<R>> base() {
		return new EvalJavaStmt<R>() {};
	}
	
	@Override
	default R Method(SD<R> body) {
		Ref<R> result = new Ref<>();
		body.accept(null, r -> {
			result.value = r;
		} , () -> {
		} , l -> {
			throw new AssertionError("Cannot call break without loop");
		} , l -> {
			throw new AssertionError("Cannot call continue without loop");
		} , exc -> {
			throw new RuntimeException(exc);
		});
		return result.value;
	}
	
}
