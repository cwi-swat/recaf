package recaf.demo.generic;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import recaf.core.alg.JavaStmtAlg;

public interface Times<R, E, S, C> extends JavaStmtAlg<R, E, S, C> {
	
	static Iterable<Integer> range(Integer n) {
		return new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return IntStream.range(0, n).iterator();
			}
		};
		
	}
	
	default S Times(Supplier<Integer> exp, S body) {
		return ForEach(() -> range(exp.get()), ignored -> body);
	}
}
