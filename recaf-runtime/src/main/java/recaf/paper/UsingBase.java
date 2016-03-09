package recaf.paper;

import java.util.function.Function;
import java.util.function.Supplier;

//BEGIN_USING_BASE
public class UsingBase<R> extends MuJavaBase<R> implements Using<R, IExec> {

	@Override
	public <T extends AutoCloseable> IExec Using(Supplier<T> r, Function<T, IExec> s) {
		return () -> {
			T t = null;
			try {
				t = r.get();
				s.apply(t).exec();
			}
			finally {
				if (t != null) {
					try {
						t.close();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		};
	}

}
//END_USING_BASE
