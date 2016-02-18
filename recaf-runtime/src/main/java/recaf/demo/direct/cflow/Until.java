package recaf.demo.direct.cflow;

import java.util.function.Supplier;

import recaf.core.direct.FullJava;
import recaf.core.direct.IExec;

public interface Until<R> extends FullJava<R> {

	default IExec Until(Supplier<Boolean> cond, IExec body) {
		return DoWhile(body, () -> !cond.get());
	}

}
