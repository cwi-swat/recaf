package recaf.demo.direct.cflow;

import java.util.function.Supplier;

import recaf.core.direct.IExec;
import recaf.core.direct.NoOp;

public class Until<R> extends NoOp<R> {

	public IExec Until(Supplier<Boolean> cond, IExec body) {
		return DoWhile(body, () -> !cond.get());
	}

}
