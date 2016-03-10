package recaf.paper;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Maybe<R, S> {
   <T> S Maybe(Supplier<Optional<T>> x, Function<T,S> s);
}
