package recaf.demo.direct;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.direct.IExec;
import recaf.core.direct.StmtJavaDirect;

public class Maybe<R> implements StmtJavaDirect<R>, JavaMethodAlg<Optional<R>, IExec> {

	@SuppressWarnings("unchecked")
	@Override
	public Optional<R> Method(IExec body) {
		try {
			body.exec(null);
		}
		catch (Return r) {
			return Optional.of((R) r.getValue());
		} 
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return Optional.empty();
	}

	public <U> IExec Maybe(Supplier<Optional<U>> opt, Function<U, IExec> body) {
		return l -> {
			Optional<U> v = opt.get();
			if (v.isPresent()) {
				body.apply(v.get()).exec(null);
			}
		};
	}
		
}
