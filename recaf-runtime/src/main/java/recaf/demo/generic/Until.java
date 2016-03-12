package recaf.demo.generic;

import java.util.function.Supplier;

import recaf.core.alg.JavaStmtAlg;

public interface Until<R, E, S, C> extends JavaStmtAlg<R, S, C>  {

	default S Until(Supplier<Boolean> cond, S body) {
		return DoWhile(body, () -> !cond.get());
	}

}
