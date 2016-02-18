package recaf.demo.direct.cflow;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import recaf.core.direct.IExec;
import recaf.core.direct.NoOp;

public class Times<R> extends NoOp<R> {
	
	private static Iterable<Integer> range(Integer n) {
		return new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return IntStream.range(0, n).iterator();
			}
		};
		
	}
	
	public IExec Times(Supplier<Integer> exp, IExec body) {
		return ForEach(() -> range(exp.get()), ignored -> body);
	}
}
