package recaf.amb;

import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class BacktrackingExtension<R> extends AbstractJavaCPS<R> {

	
	
	public SD<R> Check(ED<Boolean> cond) {
		return (rho, sigma, err) -> {
			cond.accept(b -> {
				if (!b) {
					throw new Fail();
				}
				sigma.call();
			}, err);
		};
	}
	
	public <T> SD<R> Some(ED<Iterable<T>> choices, Function<T,SD<R>> body) {
		return (rho, sigma, err) -> {
			choices.accept(cs -> {
				for (T t: cs) {
					try {
						body.apply(t).accept(rho, sigma, err);
					}
					catch (Fail e) {
						continue;
					}
				}
				throw new Fail();
			}, err);
		};
	}
	
	public <T> SD<R> Every(ED<Iterable<T>> choices, Function<T,SD<R>> body) {
		return (rho, sigma, err) -> {
			choices.accept(cs -> {
				for (T t: cs) {
					try {
						body.apply(t).accept(rho, sigma, err);
					}
					catch (Fail e) {
						continue;
					}
				}
				throw new Fail();
			}, err);
		};
	}
}
