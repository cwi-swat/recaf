package recaf.paper.full;

import java.util.function.Function;

import recaf.paper.expr.IEval;
import recaf.paper.expr.Pair;
import recaf.paper.stm.MuJava;

public interface MuStmPrintEvalAdapter<R, S> extends MuStmJava<S, Pair<IEval, String>> {
	MuJava<R, S> base();
	
	static <T> T adapt(Pair<IEval, String> e) {
		try { return (T) e.fst.eval(); } 
		catch (Throwable t) {throw new RuntimeException(t);}
	}
	
	@Override
	default S Exp(Pair<IEval, String> e) {
		return base().Exp(() -> { adapt(e); return null;});
	}
	
	@Override
	default <T> S Decl(Pair<IEval, String> x, Function<T, S> s) {
		return base().Decl(() -> adapt(x), s); 
	}
	
	@Override
	default <T> S For(Pair<IEval, String> e, Function<T, S> s) {
		return base().For(() -> (Iterable<T>) adapt(e), s);
	}
	
	@Override
	default S If(Pair<IEval, String> c, S s1, S s2) {
		return base().If(() -> adapt(c), s1, s2);
	}
	
	@Override
	default S Return(Pair<IEval, String> e) {
		return base().Return(() -> adapt(e));
	}
	
	@Override
	default S Seq(S s1, S s2) {
		return base().Seq(s1, s2);
	}
	
	@Override
	default S Empty() {
		return base().Empty();
	}
}
