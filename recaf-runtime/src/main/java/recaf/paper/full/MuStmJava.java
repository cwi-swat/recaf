package recaf.paper.full;

import java.util.function.Function;

public
//BEGIN_MUSTMJAVA
interface MuStmJava<S, E> {
	S Exp(E e);
	S Decl(E x, Function<?, S> s);
	<T> S For(E e, Function<T, S> s);
	S If(E c, S s1, S s2);
	S Return(E e);
	S Seq(S s1, S s2);
	S Empty();
}
//END_MUSTMJAVA