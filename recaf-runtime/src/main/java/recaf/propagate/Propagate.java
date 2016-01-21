package recaf.propagate;

import java.util.ArrayDeque;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class Propagate<R, T> extends AbstractJavaCPS<R> {
	private final ArrayDeque<T> stack = new ArrayDeque<>();
	
	public Reader<R, T> Method(SD<R> body) {
		return new Reader<R, T>() {
			@Override
			public R run(T t) {
				try {
					stack.push(t);
					return typePreserving(body);
				}
				finally {
					stack.pop();
				}
			}
		};
	}

	public SD<R> Local(ED<T> exp, SD<R> body) {
		return (rho, sigma, err) -> {
			exp.accept(t -> {
				stack.push(t);
				body.accept(r -> { stack.pop(); rho.accept(r);}, 
						() -> { stack.pop(); sigma.call(); }, 
						e -> { stack.pop(); err.accept(e); });
			}, err);
		};
	}

	public SD<R> Ask(Function<T, SD<R>> body) {
		return (rho, sigma, err) -> {
			body.apply(stack.peek()).accept(rho, sigma, err);
		};
	}
	
}
