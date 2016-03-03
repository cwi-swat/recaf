package recaf.demo.direct;

import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.direct.IExec;
import recaf.core.direct.NoOp;

public interface Using<R> extends NoOp<R> {

	default <U extends AutoCloseable> IExec Using(ISupply<U> resource, Function<U, IExec> body) {
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
