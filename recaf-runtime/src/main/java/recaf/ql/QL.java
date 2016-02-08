package recaf.ql;

import java.util.function.Function;
import java.util.function.Supplier;


public interface QL<Q> {

	Q Method(Q questionnaire);
	
	<T> Supplier<T> Exp(Supplier<T> t);
	
	<T> Q Question(Function<T, Q> let);

	<T> Q Question(Supplier<T> exp, Function<T, Q> let);

	Q If(Supplier<Boolean> exp, Q question);

	Q Empty();
	
}
