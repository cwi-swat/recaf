package recaf.yield;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.functional.SD;

public class YieldExtension<R> extends AbstractJavaImpl<R> {

	public Iterable<R> Method(SD<R> body) {
		
		Iterable<R> iter = new InternalIterableBase<R>(() -> {
			body.accept(r -> {}, () -> {}, l -> {}, l -> {}, ex -> {});
			return null;
		});
		
		return iter;
	}
}
