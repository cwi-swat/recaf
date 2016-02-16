package recaf.core.definitional;

import java.util.function.Function;

import recaf.core.Ref;
import recaf.core.functional.CD;
import recaf.core.functional.ED;
import recaf.core.functional.K0;
import recaf.core.functional.SD;

public interface JavaStmtAlg<E, S, C> {
	<T> S Decl(E exp, Function<Ref<T>, S> body);

	<T> S For(String label, E coll, Function<Ref<T>, S> body);
	
	S For(String label, E cond, S update, S body); 
	
	S If(E e, S s);
	
	S If(E e, S s1, S s2);
	
	S While(E e, S s);
	
	S DoWhile(S s, E e);
	
	S Labeled(String label, S s);
	
	S Break(String label);
	
	S Continue(String label);
	
	S Break();
	
	S Continue();
	
	S Return(E e);
	
	S Return();

	S Throw(E e);

	S Empty();
	
	S Seq(S s1, S s2);
	
	S TryCatch(S body, Class<? extends Throwable> type, Function<Throwable, S> handle);

	S TryFinally(S body, S fin);
	
	S ExpStat(K0 exp);
	
	S Switch(E expr, C... cases);
		
	C Case(E constant, S expStat);
	
	C Default(S expStat);
	
}
