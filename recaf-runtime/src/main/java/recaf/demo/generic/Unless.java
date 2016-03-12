package recaf.demo.generic;
import java.util.function.Supplier;

import recaf.core.alg.JavaStmtAlg;

public interface Unless<R, E, S, C> extends JavaStmtAlg<R, S, C>  {
	default S Unless(Supplier<Boolean> cond, S body) {
		return If(() -> !cond.get(), body);
	}
}
