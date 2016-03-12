package recaf.core.alg;

import java.util.function.Function;

import recaf.core.Ref;

public interface JavaStmtAlg<R, E, S, C> {
	<T> S Decl(E exp, Function<Ref<T>, S> body);

	<T> S ForEach(E exp, Function<Ref<T>, S> body);

	<T> S ForDecl(E init, Function<Ref<T>, S> body);

	S ForBody(E cond, S update, S body);

	<T extends Throwable> S Throw(E e);

	<T extends Throwable> S TryCatch(S body, Class<T> type, Function<T, S> handle);

	@SuppressWarnings("unchecked")
	<T> S Switch(E expr, C... cases);

	<T> C Case(T constant, S expStat);

	C Default(S expStat);

	S For(S init, E cond, S update, S body);

	S If(E cond, S s);

	S If(E cond, S s1, S s2);

	S While(E cond, S s);

	S DoWhile(S s, E cond);

	S Labeled(String label, S s);

	S Break(String label);

	S Continue(String label);

	S Break();

	S Continue();

	S Return(E supplier);

	S Return();

	S Empty();

	S Seq(S s1, S s2);

	S TryFinally(S body, S fin);

	S ExpStat(E e);
}
