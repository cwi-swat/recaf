package recaf.core;

public interface IRef<T> {
	T value();
	IRef<T> setValue(T val);
}
