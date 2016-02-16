package recaf.core.direct;

public class Return extends Exception {
	private final Object value;

	public Return(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
}
