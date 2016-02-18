package recaf.propagate;

import java.util.ArrayDeque;
import java.util.function.Function;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.cps.ED;
import recaf.core.cps.SD;

public class Propagate<R> extends AbstractJavaImpl<R> {
	// BAD!!!
	// non reentrant, only single type...
	// but works across ordinary methods...
	private static final ArrayDeque<Object> stack = new ArrayDeque<>();
	
	public R Method(SD<R> body) {
		return typePreserving(body);
	}

	public <T> SD<R> Local(ED<T> exp, SD<R> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			exp.accept(t -> {
				stack.push(t);
				body.accept(null, r -> { stack.pop(); rho.accept(r);}, 
						() -> { stack.pop(); sigma.call(); }, 
						l -> { throw new AssertionError("cannot break without loop"); }, 
						l -> { throw new AssertionError("cannot continue without loop"); },
						e -> { stack.pop(); err.accept(e); });
			}, err);
		};
	}

	@SuppressWarnings("unchecked")
	public <T> SD<R> Ask(Function<T, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			body.apply((T) stack.peek()).accept(null, rho, sigma, brk, contin, err);
		};
	}
	
}
