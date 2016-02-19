package recaf.core.direct;

public interface NoOp<R> extends FullJava<R> {

	@SuppressWarnings("unchecked")
	default R Method(IExec body) {
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
