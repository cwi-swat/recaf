package recaf.core.definitional;

import java.util.function.Supplier;

public class NoOp<R> implements EvalJavaStmt {

	@SuppressWarnings("unchecked")
	public R Method(IExec body) {
		try {
			body.exec(null);
		}
		catch (Return r) {
			return (R) r.getValue();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	// Should this be here, or in JavaStmtAlg?
	@SuppressWarnings("unchecked")
	public <T> Supplier<T> Exp(IEval exp) {
		return () -> (T)exp.eval();
	}
	
}
