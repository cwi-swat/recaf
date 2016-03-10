package recaf.paper;

import java.util.Optional;

public interface ToOptional<R> extends MuJavaBase<Optional<R>>{
	@Override
	default Optional<R> Method(IExec s) {
		try { 
			s.exec(); 
		} catch (Return r) { 
			return Optional.of((R)r.value); 
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return Optional.empty();
	}
}
