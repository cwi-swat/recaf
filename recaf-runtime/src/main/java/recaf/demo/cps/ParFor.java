package recaf.demo.cps;

import java.util.Collection;
import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.cps.SD;
import recaf.core.cps.StmtJavaCPS;

public class ParFor<R> implements StmtJavaCPS<R> {
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
