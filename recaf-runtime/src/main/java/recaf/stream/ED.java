package recaf.stream;

public interface ED<T, U> extends TriConsumer<StreamSubscription<U>, K<T>, K<Throwable>> {
	
}