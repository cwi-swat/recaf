package recaf.demo.direct.using;

import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.direct.FullJava;
import recaf.core.direct.IExec;

public interface Using<R> extends FullJava<R> {

	default <U extends AutoCloseable> IExec Using(Supplier<U> resource, Function<U, IExec> body) {
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
