package recaf.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.SD;
import recaf.core.cps.StmtJava;

public class AsyncExtension<R> implements StmtJava<R>, JavaMethodAlg<Future<R>, SD<R>> {

	public Future<R> Method(SD<R> body) {
		CompletableFuture<R> promise = new CompletableFuture<R>();

		CompletableFuture.supplyAsync(() -> {
			body.accept(null,
					r -> promise.complete(r), 
					() -> promise.complete(null),
					l -> {throw new AssertionError("cannot break without loop");},
					l -> {throw new AssertionError("cannot break without loop");},
					ex -> promise.completeExceptionally(ex));
			return null;
		});

		return promise;
	}

	public <T> SD<R> Await(Supplier<CompletableFuture<T>> e, Function<T, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> get(e).accept(f -> {
			f.whenComplete((a, ex) -> {
				if (a == null) {
					err.accept(ex);
				} else {
					body.apply(a).accept(null, rho, sigma, brk, contin, err);
				}
			});
		} , err);
	}
}
