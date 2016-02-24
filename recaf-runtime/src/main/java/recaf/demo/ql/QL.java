package recaf.demo.ql;

import java.util.function.Function;
import java.util.function.Supplier;

public class QL<R> {
	@SuppressWarnings("unchecked")
	public <T> Supplier<T> Exp(Supplier<?> e) {
		return (Supplier<T>) e;
	}

	public R Method(R r) {
		return r;
	}
	
	public <T> R Question(Supplier<String> label, Function<T, R> body) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> R Question(Supplier<String> label, Supplier<T> exp, Function<T, R> body) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public R If(Supplier<Boolean> cond, R body) {
		return null;
	}
	
	public R Empty() {
		return null;
	}
	

}
