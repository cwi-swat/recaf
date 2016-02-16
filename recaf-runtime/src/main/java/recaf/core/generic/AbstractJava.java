package recaf.core;

import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.functional.ED;
import recaf.core.functional.K0;
import recaf.core.functional.SD;

public interface AbstractJava<R> {

	<T> ED<T> Exp(Supplier<T> e);

	SD<R> Empty();

	SD<R> If(ED<Boolean> e, SD<R> s);

	SD<R> If(ED<Boolean> e, SD<R> s1, SD<R> s2);

	SD<R> Labeled(String label, SD<R> s);

	SD<R> While(ED<Boolean> e, SD<R> s);

	SD<R> DoWhile(SD<R> s, ED<Boolean> e);

	<S> SD<S> Switch(ED<Integer> expr, SD<S>... cases);

	SD<R> Case(ED<Integer> constant, SD<R> expStat);

	SD<R> Default(SD<R> expStat);

	SD<R> Break();

	SD<R> Break(String label);

	SD<R> Continue();

	SD<R> Continue(String label);

	SD<R> Return(ED<R> e);

	SD<R> Return();

	SD<R> Seq(SD<R>... ss);

	<T extends Throwable> SD<R> Throw(ED<T> e);

	<T extends Throwable> SD<R> TryCatch(SD<R> body, Class<T> type, Function<T, SD<R>> handle);

	SD<R> TryFinally(SD<R> body, SD<R> fin);

	<U> SD<R> ExpStat(K0 thunk);

	/*
	 * HOAS for let expressions int x = 3; s ==> Let(Exp(3), x -> [[s]]) S Let(E
	 * exp, Function<E, S> body);
	 */
	<U> SD<R> Decl(ED<U> exp, Function<U, SD<R>> body);

	<U> SD<R> For(ED<Iterable<U>> coll, Function<U, SD<R>> body);

}