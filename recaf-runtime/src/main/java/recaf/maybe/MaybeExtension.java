package recaf.maybe;

import java.util.Optional;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.Ref;
import recaf.core.SD;


public class MaybeExtension<R> extends AbstractJavaCPS<R> {

	public Optional<R> Method(SD<R> body) {
		Ref<Optional<R>> ref = new Ref<Optional<R>>();
		body.accept(r -> { ref.x = Optional.of(r); }, 
				() -> { ref.x = Optional.empty(); }, 
				exc -> { throw new RuntimeException(exc); });
		return ref.x;
	}
	
	public <U> SD<R> Maybe(ED<Optional<U>> opt, Function<U, SD<R>> body) {
		return (rho, sigma, err) -> opt.accept(v -> {
			if (v.isPresent()) {
				body.apply(v.get()).accept(rho, sigma, err);
			}
			else {
				sigma.call();
			}
		}, err);
	}
	
}
