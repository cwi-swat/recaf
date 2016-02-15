package recaf.stream;

public interface SD<R, U> {
	void accept(StreamSubscription<U> enclosing, K<R> rho, K0 sigma, K<Throwable> err);
	
}