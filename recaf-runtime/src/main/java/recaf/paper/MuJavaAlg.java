package recaf.paper;

import java.util.function.Function;

public interface MuJavaAlg<R, S, E> {
	R Method(S s);
	S Exp(E e);
	<T> S Decl(E x, Function<T, S> s);
	<T> S For(E e, Function<T, S> s);
	S If(E c, S s1, S s2);
	S Return(E e);
	S Seq(S s1, S s2);
	S Empty();
}
