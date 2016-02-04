package recaf.stream;

import recaf.core.functional.TriConsumer;

public interface ED<T, U> extends TriConsumer<StreamSubscription<U>, K<T>, K<Throwable>> {
	
}