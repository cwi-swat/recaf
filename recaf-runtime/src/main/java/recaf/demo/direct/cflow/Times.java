package recaf.demo.direct.cflow;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import recaf.core.direct.FullJava;
import recaf.core.direct.IExec;

public interface Times<R> extends FullJava<R> {
	
	static Iterable<Integer> range(Integer n) {
		return new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return IntStream.range(0, n).iterator();
			}
		};
		
	}
	
	default IExec Times(Supplier<Integer> exp, IExec body) {
		return ForEach(() -> range(exp.get()), ignored -> body);
	}
}
