package recaf.paper;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public interface Times<R, S> extends MuJava<R, S> {
	static Iterable<Integer> range(Integer n) {
		return new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return IntStream.range(0, n).iterator();
			}
		};
	}

	default S Times(Supplier<Integer> n, S s) {
		return For(() -> range(n.get()), ignored -> s);
	}
	
default 
//BEGIN_TIMES
S Times(Supplier<Integer> n, Function<Integer, S> s) {
  return For(() -> range(n.get()), s);
}
//END_TIMES
	
}
