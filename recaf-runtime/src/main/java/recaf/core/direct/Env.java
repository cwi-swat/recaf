package recaf.core.direct;

import java.util.NoSuchElementException;

public class Env  {
	private String x;
	private Object v;
	private Env parent;
	
	public Env(String x, Object v, Env parent) {
		this.x = x;
		this.v = v;
		this.parent = parent;
	}
	
	public Object lookup(String x) {
		if (x.equals(this.x)) {
			return v;
		}
		if (parent != null) {
			return parent.lookup(x);
		}
		throw new NoSuchElementException(x);
	}
	
	@Override
	public String toString() {
		if (parent != null) {
			return parent.toString() + ";" + x + ":" + v; 
		}
		return x + ":" + v;
	}
	
}
