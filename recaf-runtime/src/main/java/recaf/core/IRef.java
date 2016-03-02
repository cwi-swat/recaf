package recaf.core;

public interface IRef<T> {
	T value();
	void setValue(T val);
}
