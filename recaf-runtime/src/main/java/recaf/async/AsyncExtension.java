package recaf.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class AsyncExtension<R> extends AbstractJavaCPS<R> {

	public Future<R> Method(SD<R> body) {
		CompletableFuture<R> promise = new CompletableFuture<R>();
		
		CompletableFuture.supplyAsync(() -> {
			body.accept(r -> promise.complete(r), () -> promise.complete(null), ex -> promise.completeExceptionally(ex));
			return null;
		});	
		
		return promise;
	}
	
	public <T> SD<R> Await(ED<CompletableFuture<T>> e, Function<T, SD<R>> body) {
		return (rho, sigma, err) -> e.accept(f -> {
			f.whenComplete((a, ex) -> {
				if (a == null) {
					err.accept(ex);
				}
				else {
					body.apply(a).accept(rho,  sigma,  err);
				}
			});
		}, err);
	}
	
}
