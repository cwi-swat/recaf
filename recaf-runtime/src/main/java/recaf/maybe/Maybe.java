package recaf.maybe;

import java.util.Optional;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.Cont;
import recaf.core.Ref;
import recaf.core.functional.ED;
import recaf.core.functional.SD;


public class Maybe<R> extends AbstractJavaImpl<R> {

	public Optional<R> Method(Cont<R> body) {
		Ref<Optional<R>> ref = new Ref<Optional<R>>();
		body.statementDenotation.accept(r -> { ref.value = Optional.of(r); }, 
				() -> {  ref.value = Optional.empty(); }, (s) -> {  },  () -> {  }, 
				exc -> { throw new RuntimeException(exc); });
		return ref.value;
	}
	
	public <U> Cont<R> Maybe(Cont<Optional<U>> opt, Function<U, Cont<R>> body) {
		return Cont.fromSD((rho, sigma, brk, contin, err) -> opt.expressionDenotation.accept(v -> {
			if (v.isPresent()) {
				body.apply(v.get()).statementDenotation.accept(rho, sigma, brk, contin,err);
			}
			else {
				sigma.call();
			}
		}, err));
	}
	
}
