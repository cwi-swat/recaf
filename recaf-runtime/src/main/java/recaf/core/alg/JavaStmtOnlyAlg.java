package recaf.core.alg;

import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.Ref;

public interface JavaStmtOnlyAlg<R, S, C> {
	<T> S Decl(ISupply<T> exp, Function<Ref<T>, S> body);
	
	<T> S ForEach(ISupply<Iterable<T>> exp, Function<Ref<T>, S> body);
	
	<T> S ForDecl(ISupply<T> init, Function<Ref<T>, S> body);

	S ForBody(ISupply<Boolean> cond, S update, S body);
	
	<T extends Throwable> S Throw(ISupply<T> e);

	<T extends Throwable> S TryCatch(S body, Class<T> type, Function<T, S> handle);
	
	@SuppressWarnings("unchecked")
	<T> S Switch(ISupply<T> expr, C... cases);
		
	<T> C Case(T constant, S expStat);
	
	C Default(S expStat);

	S For(S init, ISupply<Boolean> cond, S update, S body);

	S If(ISupply<Boolean> cond, S s);
	
	S If(ISupply<Boolean> cond, S s1, S s2);
	
	S While(ISupply<Boolean> cond, S s);
	
	S DoWhile(S s, ISupply<Boolean> cond);
	
	S Labeled(String label, S s);
	
	S Break(String label);
	
	S Continue(String label);
	
	S Break();
	
	S Continue();

	S Return(ISupply<R> supplier);
	
	S Return();

	S Empty();
	
	S Seq(S s1, S s2);
	
	S TryFinally(S body, S fin);
	
	S ExpStat(ISupply<Void> e);
}
