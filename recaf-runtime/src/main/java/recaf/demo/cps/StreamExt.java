package recaf.demo.cps;

import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.SD;
import recaf.core.cps.StmtJava;
import recaf.demo.cps.stream.Stream;

public class StreamExt<R> implements StmtJava<R>, JavaMethodAlg<Stream<R>, SD<R>> {
	
	public Stream<R> Method(SD<R> body) {
		throw new UnsupportedOperationException();
	}
	
	public <T> SD<R> Await(Supplier<Stream<T>> e, Function<T, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> get(e).accept(f -> {
			throw new UnsupportedOperationException();
		} , err);
	}
	
	public <U> SD<R> Yield(Supplier<U> exp) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(exp).accept(v -> {
				throw new UnsupportedOperationException();
			} , err);
		};
	}

	public <U> SD<R> YieldFrom(Supplier<Iterable<U>> exp) {
		return ForEach(exp, e -> Yield(() -> e));
	}
}
