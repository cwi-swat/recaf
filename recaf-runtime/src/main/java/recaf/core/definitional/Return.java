package recaf.core.definitional;

public class Return extends Exception {
	private final Object value;

	public Return(Object value) {
		this.value = value;
	}
}
