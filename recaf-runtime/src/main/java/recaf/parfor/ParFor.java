package recaf.parfor;

import java.util.Collection;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.ED;
import recaf.core.SD;

public class ParFor<R> extends AbstractJavaImpl<R> {
	
	public R Method(Cont<R> body) {
		return typePreserving(body);
	}

	public <U> SD<R> Parfor(ED<Collection<U>> coll, Function<U, SD<R>> body) {
		return (rho, sigma, err) -> {
			coll.accept(v -> {
				v.parallelStream().forEach(u -> {
					body.apply(u).accept(rho, sigma, err);
				});
			}, err);
		};
	}
}
