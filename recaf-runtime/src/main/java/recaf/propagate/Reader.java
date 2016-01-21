package recaf.propagate;

public interface Reader<R, T> {
	R run(T t);
}
