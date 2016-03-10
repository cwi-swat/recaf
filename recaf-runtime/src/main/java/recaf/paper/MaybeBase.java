package recaf.paper;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class MaybeBase<R> implements Maybe<R, IExec>, ToOptional<R> {
	@Override
	public <T> IExec Maybe(Supplier<Optional<T>> x, Function<T, IExec> s) {
		return () -> {
			Optional<T> opt = x.get();
			if (opt.isPresent()) {
				s.apply(opt.get()).exec();
			}
		};
	}

}
