package recaf.paper;

import java.util.function.Function;
import java.util.function.Supplier;

//BEGIN_MUJAVA_ALG
interface MuJava<R, S> {
	R Method(S s);
	S Exp(Supplier<Void> e);
	<T> S Decl(Supplier<T> e, Function<T, S> s);
	<T> S For(Supplier<Iterable<T>> e, Function<T, S> s);
	S If(Supplier<Boolean> c, S s1, S s2);
	S Return(Supplier<? extends R> e);
	S Seq(S s1, S s2);
	S Empty();
}
//END_MUJAVA_ALG
