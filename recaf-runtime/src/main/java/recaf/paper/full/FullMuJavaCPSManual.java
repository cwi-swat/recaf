package recaf.paper.full;

import recaf.paper.expr.IEval;
import recaf.paper.expr.MuExpJavaBase;
import recaf.paper.stm.MuJavaCPS;
import recaf.paper.stm.SD;

public interface FullMuJavaCPSManual<R> extends FullMuJava<SD<R>, IEval>, MuStmJavaManualAdapter<R, SD<R>>, MuExpJavaBase {
	@Override
	default MuJavaCPS<R> base() {
		return new MuJavaCPS<R>() { };
	}
}
