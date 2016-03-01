package recaf.demo.pegs;

public class Return extends RuntimeException {
	private Object value;

	public Return(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
}
