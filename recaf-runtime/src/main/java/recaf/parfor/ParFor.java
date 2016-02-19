package recaf.parfor;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.cps.SD;
import recaf.core.cps.StmtJava;

public class ParFor<R> implements StmtJava<R> {
	
	public R Method(SD<R> body) {
		return typePreserving(body);
	}

	public <U> SD<R> Parfor(Supplier<Collection<U>> coll, Function<U, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(coll).accept(v -> {
				v.parallelStream().forEach(u -> {
					body.apply(u).accept(null,rho, sigma, brk, contin, err);
				});
			}, err);
		};
	}
}
