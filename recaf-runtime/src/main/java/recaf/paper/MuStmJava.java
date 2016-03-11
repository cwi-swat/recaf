package recaf.paper;

import java.util.function.Function;

//BEGIN_MUSTMJAVA
interface MuStmJava<R, S, E> {
	R Method(S s);
	S Exp(E e);
	S Decl(E x, Function<?, S> s);
	S For(E e, Function<?, S> s);
	S If(E c, S s1, S s2);
	S Return(E e);
	S Seq(S s1, S s2);
	S Empty();
}
//END_MUSTMJAVA