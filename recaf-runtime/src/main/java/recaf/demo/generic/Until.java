package recaf.demo.generic;

import java.util.function.Supplier;

import recaf.core.alg.JavaStmtOnlyAlg;

public interface Until<R, E, S, C> extends JavaStmtOnlyAlg<R, S, C>  {

	default S Until(Supplier<Boolean> cond, S body) {
		return DoWhile(body, () -> !cond.get());
	}

}
