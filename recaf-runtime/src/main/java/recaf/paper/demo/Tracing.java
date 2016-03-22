package recaf.paper.demo;

import recaf.paper.expr.IEval;
import recaf.paper.expr.MuExpJavaBase;

public 
interface Tracing extends MuExpJavaBase {
//BEGIN_TRACING
	default IEval Var(String x, Object v){ 
		return () -> {
			System.err.println(x + " = " + v);
			return MuExpJavaBase.super.Var(x, v).eval();
		};
	}
//END_TRACING
}

