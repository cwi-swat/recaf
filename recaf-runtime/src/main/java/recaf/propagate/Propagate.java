package recaf.propagate;

import java.util.ArrayDeque;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class Propagate<T, R> extends AbstractJavaCPS<R> {
	// BAD!!!
	// non reentrant, only single type...
	// but works across ordinary methods...
	private static final ArrayDeque<Object> stack = new ArrayDeque<>();
	
	public R Method(SD<R> body) {
		return typePreserving(body);
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

	@SuppressWarnings("unchecked")
	public SD<R> Ask(Function<T, SD<R>> body) {
		return (rho, sigma, err) -> {
			body.apply((T) stack.peek()).accept(rho, sigma, err);
		};
	}
	
}
