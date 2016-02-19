package recaf.dummy;

import recaf.core.cps.SD;
import recaf.core.cps.StmtJava;

public class DummyExtension<R> implements StmtJava<R> {
	public R Method(SD<R> body) {
		return typePreserving(body);
	}
}
