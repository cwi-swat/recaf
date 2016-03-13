package recaf.core.full;

import static recaf.core.expr.EvalJavaHelper.toValue;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.alg.JavaStmtOnlyAlg;
import recaf.core.direct.EvalJavaStmt;
import recaf.core.direct.ICase;
import recaf.core.direct.IExec;
import recaf.core.expr.EvalJavaExpr;


public interface FullJavaDirect<R> extends JavaStmtAlgAdapter<R, IExec, ICase>, EvalJavaExpr, JavaMethodAlg<R, IExec> {

	@Override
	default JavaStmtOnlyAlg<R, IExec, ICase> base() {
		return new EvalJavaStmt<R>() {};
	}
	
	@SuppressWarnings("unchecked")
	@Override
	default R Method(IExec body) {
		try {
			body.exec(null);
		}
		catch (EvalJavaStmt.Return r) {
			return (R) toValue(r.getValue());
		} 
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}
