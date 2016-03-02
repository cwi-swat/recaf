package recaf.demo.direct;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.direct.IExec;
import recaf.core.direct.IExecEx;
import recaf.core.direct.StmtJava;

public class Maybe<R> implements StmtJava<R>, JavaMethodAlg<Optional<R>, IExecEx<?>> {

	@SuppressWarnings("unchecked")
	@Override
	public Optional<R> Method(IExecEx<?> body) {
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

	public <U> IExecEx<?> Maybe(Supplier<Optional<U>> opt, Function<U, IExecEx<?>> body) {
		return l -> {
			Optional<U> v = opt.get();
			if (v.isPresent()) {
				body.apply(v.get()).exec(null);
			}
		};
	}
		
}
