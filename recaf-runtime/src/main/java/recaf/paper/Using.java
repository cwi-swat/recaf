package recaf.paper;

import java.util.function.Function;
import java.util.function.Supplier;

//BEGIN_USING_ALG
interface Using<R, S> {
	<T extends AutoCloseable> S Using(Supplier<T> r, Function<T, S> s);
}
//END_USING_ALG
