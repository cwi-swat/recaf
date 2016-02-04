package recaf.stream;

import recaf.core.functional.QuadriConsumer;

public interface SD<R, U> extends QuadriConsumer<StreamSubscription<U>, K<R>, K0, K<Throwable>> {
	
}