package recaf.core.expr;

public interface IRef<T> {
	T value();
	IRef<T> setValue(T val);
}
