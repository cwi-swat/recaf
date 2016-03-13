package recaf.paper.demo;

import recaf.paper.expr.IEval;
import recaf.paper.expr.MuExpJavaBase;
import recaf.paper.full.MuStmJavaAdapter;
import recaf.paper.stm.IExec;

public interface OrElse<R> extends MuStmJavaAdapter<R, IExec>, MuExpJavaBase {
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
