package recaf.core.alg;

import recaf.core.ISupply;

public interface JavaStmtOnlyNoHOASAlg<R, S, C> {
	<T> S Decl(String x, ISupply<T> exp, S body);
	
	<T> S ForEach(String x, ISupply<Iterable<T>> exp, S body);
	
	<T> S ForDecl(String x, ISupply<T> init, S body);

	S ForBody(ISupply<Boolean> cond, S update, S body);
	
	<T extends Throwable> S Throw(ISupply<T> e);

	<T extends Throwable> S TryCatch(S body, Class<T> type, String x, S handle);
	
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
