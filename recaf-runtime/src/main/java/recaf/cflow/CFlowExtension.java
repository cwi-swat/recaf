package recaf.cflow;

import java.util.Iterator;
import java.util.stream.IntStream;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class CFlowExtension<R> extends AbstractJavaCPS<R> {
	public R Method(SD<R> body) {
		return typePreserving(body);
	}
	
	public SD<R> Until(ED<Boolean> cond, SD<R> body) {
		return Seq2(body, While((k, err) -> cond.accept(b -> k.accept(!b), err), body));
	}
	
	public SD<R> Loop(SD<R> body) {
		return While(Exp(() -> true), body);
	}
	
	public SD<R> Times(ED<Integer> n, SD<R> body) {
		return this.<Integer>For((k, err) -> n.accept(v -> k.accept(new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return IntStream.range(0, v).iterator();
			}
		}), err), i /* ingored */ -> body);
	}

	public SD<R> Unless(ED<Boolean> cond, SD<R> body) {
		return While((k, err) -> cond.accept(b -> k.accept(!b), err), body);
	}
	
}
