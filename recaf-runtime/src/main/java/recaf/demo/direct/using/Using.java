package recaf.demo.direct.using;

import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.direct.IExec;
import recaf.core.direct.NoOp;

public class Using<R> extends NoOp<R> {

	
	public <U extends AutoCloseable> IExec Using(Supplier<U> resource, Function<U, IExec> body) {
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
