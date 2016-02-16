package recaf.dummy;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.functional.SD;

public class DummyExtension<R> extends AbstractJavaImpl<R> {

	public R Method(SD<R> body) {
		return typePreserving(body);
	}
}
