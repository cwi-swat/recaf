package recaf.coro;

public interface Co<T, U> {
	U resume(T t);
	void run();
}
