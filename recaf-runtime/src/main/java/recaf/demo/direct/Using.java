package recaf.demo.direct;

import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.direct.IExec;
import recaf.core.direct.IExecEx;
import recaf.core.direct.NoOp;

public interface Using<R> extends NoOp<R> {

	default <U extends AutoCloseable> IExecEx<?> Using(Supplier<U> resource, Function<U, IExecEx<?>> body) {
		return l -> {
			U u = null;
			try {
				u = resource.get();
				body.apply(u).exec(null);
			}
			finally {
				if (u != null) {
					u.close();
				}
			}
		};
	}

}
