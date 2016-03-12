package recaf.paper.full;

import java.util.function.Function;
import java.util.function.Supplier;

import recaf.paper.expr.IEval;
import recaf.paper.stm.MuJava;

public interface MuStmJavaAdapter<R, S> extends MuStmJava<S, IEval> {
	MuJava<R, S> base();
	
	static <T> T eval(IEval e) {
		try { return (T) e.eval(); } 
		catch (Throwable t) { throw new RuntimeException(t); }
	}
	
	@Override
	default S Exp(IEval e) {
		return base().Exp(() -> { eval(e); return null;});
	}
	
	@Override
	default S Decl(IEval x, Function<?, S> s) {
		return base().Decl(() -> eval(x), s); 
	}
	
	@Override
	default <T> S For(IEval e, Function<T, S> s) {
		return base().For(() -> (Iterable<T>) eval(e), s);
	}
	
	@Override
	default S If(IEval c, S s1, S s2) {
		return base().If(() -> eval(c), s1, s2);
	}
	
	@Override
	default S Return(IEval e) {
		return base().Return(() -> eval(e));
	}
	
	default S Seq(S s1, S s2) {
		return base().Seq(s1, s2);
	}
	
	default S Empty() {
		return base().Empty();
	}
}
