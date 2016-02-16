package recaf.cflow;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.cps.SD;

public abstract class CFlow<R> extends AbstractJavaImpl<R> {
	public R Method(SD<R> body) {
		return typePreserving(body);
	}
}
