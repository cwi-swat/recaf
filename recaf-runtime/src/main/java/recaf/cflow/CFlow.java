package recaf.cflow;

import recaf.core.AbstractJavaCPS;
import recaf.core.SD;

public abstract class CFlow<R> extends AbstractJavaCPS<R> {
	public R Method(SD<R> body) {
		return typePreserving(body);
	}
}
