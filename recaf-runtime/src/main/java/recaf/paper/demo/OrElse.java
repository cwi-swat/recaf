package recaf.paper.demo;

import recaf.paper.expr.IEval;
import recaf.paper.expr.MuExpJavaBase;
import recaf.paper.full.MuStmIEvalJavaAdapter;
import recaf.paper.full.MuStmJavaManualAdapter;
import recaf.paper.stm.IExec;

public interface OrElse<R> extends MuStmIEvalJavaAdapter<R, IExec>, MuExpJavaBase {
	//BEGIN_ORELSE
	default IEval OrElse(IEval ...es) {
		return () -> {
			for (IEval e: es) {
				Object x = e.eval();
				if (x != null) return x;
			}
			return null;
		};
	}
	//END_ORELSE
}
