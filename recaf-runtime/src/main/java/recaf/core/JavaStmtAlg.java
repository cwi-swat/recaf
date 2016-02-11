package recaf.core;

import java.util.function.Function;
import java.util.function.Supplier;

public interface JavaStmtAlg<S, R> {
	S Exp(Supplier<?> e);
	
	<T> S Decl(Supplier<T> exp, Function<T, S> body);

	<T extends Iterable<?>> S For(Supplier<T> coll, Function<Ref<T>, S> body);
	
	S If(Supplier<Boolean> e, S s);
	
	S If(Supplier<Boolean> e, S s1, S s2);
	
	S While(Supplier<Boolean> e, S s);
	
	S DoWhile(S s, Supplier<Boolean> e);
	
	S Labeled(String label, S s);
	
	S Break(String label);
	
	S Continue(String label);
	
	S Break();
	
	S Continue();
	
	S Return(Supplier<R> e);
	
	S Return();

	S Throw(Supplier<? extends Throwable> e);

	S Empty();
	
	S Seq(S s1, S s2);
	
	<T extends Throwable> S TryCatch(S body, Class<T> type, Function<? super T, S> handle);

	S TryFinally(S body, S fin);
	
}
