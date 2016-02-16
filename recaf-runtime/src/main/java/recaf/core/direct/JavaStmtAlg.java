package recaf.core.direct;

import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.Ref;
import recaf.core.functional.K0;

public interface JavaStmtAlg<E, S, C> {

	// inject E into Supplier of some T
	<T> Supplier<T> Exp(E e);
	
	<T> S Decl(Supplier<T> exp, Function<Ref<T>, S> body);

	<T> S For(String label, Supplier<Iterable<T>> exp, Function<Ref<T>, S> body);
	
	<T> S Return(Supplier<T> e);
	
	<T extends Throwable> S Throw(Supplier<T> e);

	<T extends Throwable> S TryCatch(S body, Class<T> type, Function<T, S> handle);
	
	@SuppressWarnings("unchecked")
	<T> S Switch(Supplier<T> expr, C... cases);
		
	<T> C Case(T constant, S expStat);
	
	C Default(S expStat);

	S For(String label, Supplier<Boolean> cond, S update, S body); 
	
	S If(Supplier<Boolean> cond, S s);
	
	S If(Supplier<Boolean> cond, S s1, S s2);
	
	S While(Supplier<Boolean> cond, S s);
	
	S DoWhile(S s, Supplier<Boolean> cond);
	
	S Labeled(String label, S s);
	
	S Break(String label);
	
	S Continue(String label);
	
	S Break();
	
	S Continue();

	S Return();

	S Empty();
	
	S Seq(S s1, S s2);
	
	S TryFinally(S body, S fin);
	
	
	// what do we do here if expression are desugared?
	S ExpStat(K0 exp);
	
	
}
