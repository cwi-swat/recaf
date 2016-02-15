package recaf.stream;

public interface ED<T, U> {
	void accept(StreamSubscription<U> enclosing, K<T> sigma, K<Throwable> err);
}