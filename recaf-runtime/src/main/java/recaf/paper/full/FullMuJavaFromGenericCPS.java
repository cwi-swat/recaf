package recaf.paper.full;

import recaf.paper.expr.IEval;
import recaf.paper.expr.MuExpJavaBase;
import recaf.paper.stm.MuJava;
import recaf.paper.stm.MuJavaCPS;
import recaf.paper.stm.SD;

public interface FullMuJavaFromGenericCPS<R> extends FullMuJava<SD<R>, IEval>,  MuStmIEvalJavaAdapter<R,SD<R>>, MuExpJavaBase {
	@Override
	default MuJava<R, SD<R>> base() {
		return new MuJavaCPS<R>() {};
	}

}
