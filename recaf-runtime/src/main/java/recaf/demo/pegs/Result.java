package recaf.demo.pegs;

public class Result<T> {
	private T val;
	private int pos;

	Result(T val, int pos) {
		this.val = val;
		this.pos = pos;
	}

	public T getValue() {
		return val;
	}
	
	public int getPos() {
		return pos;
	}
}
