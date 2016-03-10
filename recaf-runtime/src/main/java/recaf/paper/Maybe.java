package recaf.paper;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

//BEGIN_MAYBE
interface Maybe<R> {
	default <T> IExec Maybe(Supplier<Optional<T>> x, Function<T, IExec> s) {
		return () -> {
			Optional<T> opt = x.get();
			if (opt.isPresent()) s.apply(opt.get()).exec();
		};
	}
}
//END_MAYBE
