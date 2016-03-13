package recaf.demo.direct;

import recaf.core.direct.IEval;
import recaf.core.full.FullJavaDirect;

public interface OrElse<R> extends FullJavaDirect<R> {
	
	default IEval OrElse(IEval ...es) {
		return () -> {
			for (int i = 0; i < es.length; i++) {
				Object x = es[i].eval();
				if (x != null) {
					return x;
				}
			}
			return null;
		};
	}
	
}
