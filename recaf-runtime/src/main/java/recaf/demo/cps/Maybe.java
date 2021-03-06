package recaf.demo.cps;

import java.util.Optional;
import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.Ref;
import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.EvalJavaStmt;
import recaf.core.cps.SD;


public class Maybe<R> implements EvalJavaStmt<R>, JavaMethodAlg<Optional<R>, SD<R>> {

	@Override
	public Optional<R> Method(SD<R> body) {
		Ref<Optional<R>> ref = new Ref<Optional<R>>();
		body.accept(null, r -> { ref.value = Optional.of(r); }, 
				() -> {  ref.value = Optional.empty(); }, 
				l -> { throw new AssertionError("cannot break without loop"); }, 
				l -> { throw new AssertionError("cannot continue without loop"); }, 
				exc -> { throw new RuntimeException(exc); });
		return ref.value;
	}
	
	public <U> SD<R> Maybe(ISupply<Optional<U>> opt, Function<U, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> get(opt).accept(v -> {
			if (v.isPresent()) {
				body.apply(v.get()).accept(null, rho, sigma, brk, contin,err);
			}
			else {
				sigma.call();
			}
		}, err);
	}
	
}
