package recaf.asynciter;

public interface ED<T, U> extends TriConsumer<StreamSubscription<U>, K<T>, K<Throwable>> {
	
}