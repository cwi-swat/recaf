package recaf.core.direct;

public class NoOp<R> implements FullJava<R> {

	@SuppressWarnings("unchecked")
	public R Method(IExec body) {
		try {
			body.exec(null);
		}
		catch (Return r) {
			return (R) r.getValue();
		} 
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
}
