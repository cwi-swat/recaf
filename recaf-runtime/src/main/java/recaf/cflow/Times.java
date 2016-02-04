package recaf.cflow;

import java.util.Iterator;
import java.util.stream.IntStream;

import recaf.core.Cont;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class Times<R> extends CFlow<R> {
	public Cont<R> Times(Cont<Integer> n, Cont<R> body) {
		return this.<Integer>For(Cont.fromED((k, err) -> n.expressionDenotation.accept(v -> k.accept(new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return IntStream.range(0, v).iterator();
			}
		}), err)), i /* ignored */ -> body);
	}
}
