package recaf.paper;

import java.util.function.Function;

public interface MuStmJavaAdapter<R, S> extends MuStmJava<R, S, IEval> {
	R Method(S s);

	MuJava<R, S> base();
	
	static <T> T eval(IEval e) {
		try { return (T)e.eval(); } 
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
	default S For(IEval e, Function<?, S> s) {
		return base().For(() -> (Iterable)eval(e), s);
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
