package recaf.core.definitional;

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
	
	public IEval Exp(IEval e) {
		return e;
	}
	
}
