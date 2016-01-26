package recaf.asynciter;

public interface TriConsumer<A, B, C> {
	void accept(A a, B b, C c);
}