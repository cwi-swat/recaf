package recaf.coro;

import java.util.ArrayDeque;
import java.util.function.Function;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.cps.ED;
import recaf.core.cps.K0;
import recaf.core.cps.SD;

public class Coroutine<R, T> extends AbstractJavaImpl<R> {

	private ArrayDeque<T> stack = new ArrayDeque<>();
	
	public Co<T, R> Method(SD<R> body) {
		return new Co<T,R>() {
			private boolean exhausted = false;
			private R result;
			private K0 code = null;
			
			@Override
			public R resume(T t) {
				try {
					if (code == null) {
						throw new RuntimeException("Cannot resume if not started");
					}
					if (exhausted) {
						throw new RuntimeException("Exhausted");
					}
 					stack.push(t);
					code.call();
					return result;
				}
				catch (Yield y) {
					code = y.body;
					R prev = result;
					result = (R) y.value; 
					return prev;
				}
			}
			
			@Override
			public void run() {
				try {
					body.accept(null, r -> {this.result = r;}, 
							() -> { exhausted = true; }, 
							l -> {}, 
							l -> {}, 
							exc -> { exhausted = true; throw new RuntimeException(exc); });
				}
				catch (Yield y) {
					code = y.body;
					result = (R) y.value;
				}
				
			}
		};
	}
	
	@SuppressWarnings({"serial"})
	private final static class Yield extends RuntimeException {

		Object value;
		K0 body;

		public Yield(Object v, K0 body) {
			this.value = v;
			this.body = body;
		}
	}

	public SD<R> Yield(ED<R> exp) {
		return Yield(exp, t -> Empty());
	}
	
	public SD<R> Yield(ED<R> exp, Function<T, SD<R>> body) {
		return (label, rho, sigma, contin, brk, err) -> {
			exp.accept(v -> {
				throw new Yield(v, () -> body.apply(stack.pop()).accept(null, rho, sigma, contin, brk, err));
			}, err);
		};
	}
	
}
