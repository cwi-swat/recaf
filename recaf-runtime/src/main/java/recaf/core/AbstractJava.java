package recaf.core;

import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.functional.SD;

public interface AbstractJava<R> {

	/*
	 * 
	 * */
	
	<T> Cont<T> Exp(Supplier<T> e);
	
	<U> Cont<R> ExpStat(Cont<U> e);

	<U> Cont<R> Decl(Cont<U> exp, Function<U, Cont<R>> body);
	
	Cont<R> Seq(Cont<R>... ss);

	Cont<R> Empty();

	Cont<R> If(Cont<Boolean> e, Cont<R> s);

	Cont<R> If(Cont<Boolean> e, Cont<R> s1, Cont<R> s2);

	Cont<R> Labeled(String label, Cont<R> s);

	Cont<R> While(Cont<Boolean> e, Cont<R> s);

	Cont<R> DoWhile(Cont<R> s, Cont<Boolean> e);
	
	<U> Cont<R> For(Cont<Iterable<U>> coll, Function<U, Cont<R>> body);

	Cont<R> Break();

	Cont<R> Break(String label);

	Cont<R> Continue();

	Cont<R> Continue(String label);

	Cont<R> Return(Cont<R> e);

	Cont<R> Return();

	<T extends Throwable> Cont<R> Throw(Cont<T> e);

	<T extends Throwable> Cont<R> TryCatch(Cont<R> body, Class<T> type, Function<T, Cont<R>> handle);

	Cont<R> TryFinally(Cont<R> body, SD<R> fin);
}