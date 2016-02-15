package recaf.cflow;

import java.util.Iterator;
import java.util.stream.IntStream;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class Times<R> extends CFlow<R> {
	public SD<R> Times(ED<Integer> n, SD<R> body) {
		return this.<Integer>For(null, (k, err) -> n.accept(v -> k.accept(new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return IntStream.range(0, v).iterator();
			}
		}), err), i /* ignored */ -> body);
	}
}
