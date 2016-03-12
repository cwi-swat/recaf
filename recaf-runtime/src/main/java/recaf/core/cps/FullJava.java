package recaf.core.cps;

import recaf.core.alg.JavaStmtAlg;
import recaf.core.direct.EvalJavaExpr;

public interface FullJava<R> extends JavaStmtAlg<R, SD<R>, CD<R>>, EvalJavaExpr {
	
}