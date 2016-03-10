package recaf.paper;

public interface TypePreserving<R> extends MuJavaBase<R> {
	default R Method(IExec s) {
		try { 
			s.exec(); 
		} catch (Return r) { 
			return (R)r.value; 
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}
