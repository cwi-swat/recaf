package recaf.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class AsyncExtension<R> extends AbstractJavaImpl<R> {

	public Future<R> Method(SD<R> body) {
		CompletableFuture<R> promise = new CompletableFuture<R>();

		CompletableFuture.supplyAsync(() -> {
			body.accept(
					r -> promise.complete(r), 
					() -> promise.complete(null),
					l -> {throw new AssertionError("cannot break without loop");},
					l -> {throw new AssertionError("cannot break without loop");},
					ex -> promise.completeExceptionally(ex));
			return null;
		});

		return promise;
	}

	public <T> SD<R> Await(ED<CompletableFuture<T>> e, Function<T, SD<R>> body) {
		return (rho, sigma, brk, contin, err) -> e.accept(f -> {
			f.whenComplete((a, ex) -> {
				if (a == null) {
					err.accept(ex);
				} else {
					body.apply(a).accept(rho, sigma, brk, contin, err);
				}
			});
		} , err);
	}
}
