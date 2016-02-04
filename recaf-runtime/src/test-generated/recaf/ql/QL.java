package recaf.ql;

import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class QL extends AbstractJavaCPS<Void> {
	
	public Void Method(SD<Void> body) {
		return typePreserving(body);
	}

	public <U> SD<Void> Question(Function<U, SD<Void>> body) {
		return null;
	}

	public <U> SD<Void> Question(ED<U> exp, Function<U, SD<Void>> body) {
		return null;
	}
}
