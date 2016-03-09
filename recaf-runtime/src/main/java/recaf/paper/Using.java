package recaf.paper;

import java.util.function.Function;
import java.util.function.Supplier;

interface Using<R, S> extends MuJava<R, S> {
	<T extends AutoCloseable> S Using(Supplier<T> r, Function<T, S> s);
}
