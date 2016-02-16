package recaf.core.generic;

import java.util.function.Function;
import java.util.function.Supplier;

import higher.App;
import recaf.core.cps.K0;

public interface AbstractJava<R, S, C, E> {

	<T> App<E, T> Exp(Supplier<T> e);

	App<S, R> Empty();

	App<S, R> If(App<E, Boolean> e, App<S, R> s);

	App<S, R> If(App<E, Boolean> e, App<S, R> s1, App<S, R> s2);

	App<S, R> Labeled(String label, App<S, R> s);

	App<S, R> While(App<E, Boolean> e, App<S, R> s);

	App<S, R> DoWhile(App<S, R> s, App<E, Boolean> e);

	App<S, R> Switch(App<E, Integer> expr, App<C, R>... cases);

	App<C, R> Case(App<E, Integer> constant, App<S, R> expStat);

	App<C, R> Default(App<S, R> expStat);

	App<S, R> Break();

	App<S, R> Break(String label);

	App<S, R> Continue();

	App<S, R> Continue(String label);

	App<S, R> Return(App<E, R> e);

	App<S, R> Return();

	App<S, R> Seq(App<S, R>... ss);

	<T extends Throwable> App<S, R> Throw(App<E, T> e);

	<T extends Throwable> App<S, R> TryCatch(App<S, R> body, Class<T> type, Function<T, App<S, R>> handle);

	App<S, R> TryFinally(App<S, R> body, App<S, R> fin);

	<U> App<S, R> ExpStat(K0 thunk);

	/*
	 * HOAS for let expressions int x = 3; s ==> Let(Exp(3), x -> [[s]]) S Let(E
	 * exp, Function<E, S> body);
	 */
	<U> App<S, R> Decl(App<E, U> exp, Function<U, App<S, R>> body);

	<U> App<S, R> For(App<E, Iterable<U>> coll, Function<U, App<S, R>> body);

}