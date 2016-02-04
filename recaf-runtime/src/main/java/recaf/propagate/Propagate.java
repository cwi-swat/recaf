package recaf.propagate;

import java.util.ArrayDeque;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.Cont;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class Propagate<T, R> extends AbstractJavaImpl<R> {
	// BAD!!!
	// non reentrant, only single type...
	// but works across ordinary methods...
	private static final ArrayDeque<Object> stack = new ArrayDeque<>();
	
	public R Method(Cont<R> body) {
		return typePreserving(body);
	}

	public Cont<R> Local(Cont<T> exp, Cont<R> body) {
		return Cont.fromSD((rho, sigma, brk, contin, err) -> {
			exp.expressionDenotation.accept(t -> {
				stack.push(t);
				body.statementDenotation.accept(r -> { stack.pop(); rho.accept(r);}, 
						() -> { stack.pop(); sigma.call(); }, () -> { }, () -> { },
						e -> { stack.pop(); err.accept(e); });
			}, err);
		});
	}

	@SuppressWarnings("unchecked")
	public Cont<R> Ask(Function<T, Cont<R>> body) {
		return Cont.fromSD((rho, sigma, brk, contin, err) -> {
			body.apply((T) stack.peek()).statementDenotation.accept(rho, sigma, brk, contin, err);
		});
	}
	
}
