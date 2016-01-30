package recaf.cflow;

import recaf.core.AbstractJavaImpl;
import recaf.core.SD;

public abstract class CFlow<R> extends AbstractJavaImpl<R> {
	public R Method(Cont<R> body) {
		return typePreserving(body);
	}
}
