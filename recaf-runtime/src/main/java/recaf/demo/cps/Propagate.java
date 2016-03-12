package recaf.demo.cps;

import java.util.ArrayDeque;
import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.cps.SD;
import recaf.core.cps.StmtJavaCPS;

public class Propagate<R> implements StmtJavaCPS<R> {
	// BAD!!!
	// non reentrant, only single type...
	// but works across ordinary methods...
	private static final ArrayDeque<Object> stack = new ArrayDeque<>();
	
	public <T> SD<R> Local(ISupply<T> exp, SD<R> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(exp).accept(t -> {
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
