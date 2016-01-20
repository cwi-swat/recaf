package recaf.yield;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class YieldExtension<R> extends AbstractJavaCPS<R> {

	public Iterable<R> Method(SD<R> body) {
		
		Iterable<R> iter = new InternalIterableBase<R>(() -> {
			body.accept(r -> {}, () -> {}, ex -> {});
			return null;
		});
		
		return iter;
	}
}
