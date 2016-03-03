package recaf.demo.cps;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.ISupply;
import recaf.core.cps.NoOp;
import recaf.core.cps.SD;

public class ParFor<R> implements NoOp<R> {
	public <U> SD<R> Parfor(ISupply<Collection<U>> coll, Function<U, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(coll).accept(v -> {
				v.parallelStream().forEach(u -> {
					body.apply(u).accept(null,rho, sigma, brk, contin, err);
				});
			}, err);
		};
	}
}
