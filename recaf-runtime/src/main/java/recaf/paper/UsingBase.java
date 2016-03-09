package recaf.paper;

import java.util.function.Function;
import java.util.function.Supplier;

//BEGIN_USING_BASE
class UsingBase<R> extends MuJavaBase<R> 
  implements Using<R, IExec> {
	public <T extends AutoCloseable> 
	  IExec Using(Supplier<T> r, Function<T, IExec> s) {
		return () -> {
			T t = null;
			try { t = r.get(); s.apply(t).exec(); }
			finally { if (t != null) t.close(); }
		};
	}
}
//END_USING_BASE
