package recaf.paper.full;

import java.util.function.Function;

import recaf.paper.stm.MuJava;

//BEGIN_MU_STM_GENERIC_ADAPTER
public interface MuStmGenericJavaAdapter<R, S, E> extends MuStmJava<S, E> {
	MuJava<R, S> base();
	
	<T> T adapt(E e);
	
	@Override
	default S Exp(E e) {
		return base().Exp(() -> { adapt(e); return null;});
	}
	
	@Override
	default <T> S Decl(E x, Function<T, S> s) {
		return base().Decl(() -> adapt(x), s); 
	}
	
	@Override
	default <T> S For(E e, Function<T, S> s) {
		return base().For(() -> (Iterable<T>) adapt(e), s);
	}
	
	@Override
	default S If(E c, S s1, S s2) {
		return base().If(() -> adapt(c), s1, s2);
	}
	
	@Override
	default S Return(E e) {
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
//END_MU_STM_GENERIC_ADAPTER
