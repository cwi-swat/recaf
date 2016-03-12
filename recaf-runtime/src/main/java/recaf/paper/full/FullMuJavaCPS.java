package recaf.paper.full;

import recaf.paper.expr.IEval;
import recaf.paper.stm.MuJava;
import recaf.paper.stm.MuJavaCPS;
import recaf.paper.stm.SD;

public interface FullMuJavaCPS<R> extends FullMuJava<SD<R>, IEval>, MuStmJavaAdapter<R, SD<R>> {
	@Override
	default MuJava<R, SD<R>> base() {
		return new MuJavaCPS<R>() { };
	}
}
