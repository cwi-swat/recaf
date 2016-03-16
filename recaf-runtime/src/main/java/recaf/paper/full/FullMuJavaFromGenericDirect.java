package recaf.paper.full;

import recaf.paper.expr.IEval;
import recaf.paper.stm.IExec;
import recaf.paper.stm.MuJava;
import recaf.paper.stm.MuJavaBase;

public interface FullMuJavaFromGenericDirect<R> extends FullMuJava<IExec, IEval>,  MuStmIEvalJavaAdapter<R,IExec> {
	@Override
	default MuJava<R, IExec> base() {
		return new MuJavaBase<R>() {};
	}

}
