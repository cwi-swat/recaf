package recaf.paper.full;

import java.util.function.Function;

public
//BEGIN_MUSTMJAVA
interface MuStmJava<S, E> {
	S Exp(E x);
	<T> S Decl(E x,Function<T,S> s);
	<T> S For(E x,Function<T,S> s);
	S If(E c, S s1, S s2);
	S Return(E x);
	S Seq(S s1, S s2);
	S Empty();
}
//END_MUSTMJAVA