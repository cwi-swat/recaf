package recaf.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.Cont;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class AsyncExtension<R> extends AbstractJavaImpl<R> {

	public Future<R> Method(Cont<R> body) {
		CompletableFuture<R> promise = new CompletableFuture<R>();

		CompletableFuture.supplyAsync(() -> {
			body.statementDenotation.accept(
					r -> promise.complete(r), 
					() -> promise.complete(null),
					(s) -> promise.complete(null),
					() -> promise.complete(null),
					ex -> promise.completeExceptionally(ex));
			return null;
		});

		return promise;
	}

	public <T> Cont<R> Await(Cont<CompletableFuture<T>> e, Function<T, Cont<R>> body) {
		return Cont.fromSD((rho, sigma, brk, contin, err) -> e.expressionDenotation.accept(f -> {
			f.whenComplete((a, ex) -> {
				if (a == null) {
					err.accept(ex);
				} else {
					body.apply(a).statementDenotation.accept(rho, sigma, brk, contin, err);
				}
			});
		} , err));
	}
}
