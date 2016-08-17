package recaf.core.cps.effects;

public class Done<T> implements Effect{
	T value;

	public Done(T value) {
		super();
		this.value = value;
	}
}
