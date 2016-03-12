package recaf.demo.generic;
import java.util.function.Supplier;

import recaf.core.alg.JavaStmtOnlyAlg;

public interface Unless<R, E, S, C> extends JavaStmtOnlyAlg<R, S, C>  {
	default S Unless(Supplier<Boolean> cond, S body) {
		return If(() -> !cond.get(), body);
	}
}
