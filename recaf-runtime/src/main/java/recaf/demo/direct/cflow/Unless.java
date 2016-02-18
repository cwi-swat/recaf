package recaf.demo.direct.cflow;
import java.util.function.Supplier;

import recaf.core.direct.FullJava;
import recaf.core.direct.IExec;

public interface Unless<R> extends FullJava<R> {
	default IExec Unless(Supplier<Boolean> cond, IExec body) {
		return If(() -> !cond.get(), body);
	}
}
