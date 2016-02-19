package recaf.dummy;

import recaf.core.cps.EvalJavaStmt;
import recaf.core.cps.SD;

public class DummyExtension<R> extends EvalJavaStmt<R> {

	public R Method(SD<R> body) {
		return typePreserving(body);
	}
}
