package recaf.yield;

import recaf.core.AbstractJavaImpl;
import recaf.core.ED;
import recaf.core.SD;

public class YieldExtension<R> extends AbstractJavaImpl<R> {

	public Iterable<R> Method(SD<R> body) {
		
		Iterable<R> iter = new InternalIterableBase<R>(() -> {
			body.accept(r -> {}, () -> {}, ex -> {});
			return null;
		});
		
		return iter;
	}
}
