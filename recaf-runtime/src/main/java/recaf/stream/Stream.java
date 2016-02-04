package recaf.stream;

import java.util.function.Consumer;

import recaf.core.functional.K0;

public interface Stream<T> {

	StreamSubscription<T> listen(Consumer<T> onData, Consumer<Exception> orError, K0 onDone);
}
