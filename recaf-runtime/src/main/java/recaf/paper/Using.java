package recaf.paper;

import java.util.function.Function;
import java.util.function.Supplier;

//BEGIN_USING
interface Using<R> {
	default <T extends AutoCloseable> 
	  IExec Using(Supplier<T> r, Function<T, IExec> s) {
		return () -> {
			T t = null;
			try { t = r.get(); s.apply(t).exec(); }
			finally { if (t != null) t.close(); }
		};
	}
}
//END_USING
