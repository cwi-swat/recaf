package recaf.maybe;

import java.util.Optional;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.Ref;
import recaf.core.functional.ED;
import recaf.core.functional.SD;


public class Maybe<R> extends AbstractJavaImpl<R> {

	public Optional<R> Method(SD<R> body) {
		Ref<Optional<R>> ref = new Ref<Optional<R>>();
		body.accept(r -> { ref.value = Optional.of(r); }, 
				() -> {  ref.value = Optional.empty(); }, (s) -> {  },  () -> {  }, 
				exc -> { throw new RuntimeException(exc); });
		return ref.value;
	}
	
	public <U> SD<R> Maybe(Class<U> klass, ED<Optional<U>> opt, Function<U, SD<R>> body) {
		return (rho, sigma, brk, contin, err) -> opt.accept(v -> {
			if (v.isPresent()) {
				body.apply(v.get()).accept(rho, sigma, brk, contin,err);
			}
			else {
				sigma.call();
			}
		}, err);
	}
	
}
