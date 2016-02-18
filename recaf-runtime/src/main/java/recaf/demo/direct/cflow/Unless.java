package recaf.demo.direct.cflow;
import java.util.function.Supplier;

import recaf.core.direct.IExec;
import recaf.core.direct.NoOp;

public class Unless<R> extends NoOp<R> {
	public IExec Unless(Supplier<Boolean> cond, IExec body) {
		return If(() -> !cond.get(), body);
	}
}
