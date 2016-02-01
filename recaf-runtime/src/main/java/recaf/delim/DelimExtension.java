package recaf.delim;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.ED;
import recaf.core.SD;

public class DelimExtension<R> extends AbstractJavaImpl<R> {

	public Future<R> Method(Cont<R> body) {
		CompletableFuture<R> promise = new CompletableFuture<R>();

		CompletableFuture.supplyAsync(() -> {
			body.statementDenotation.accept(
					r -> promise.complete(r), 
					() -> promise.complete(null),
					ex -> promise.completeExceptionally(ex));
			return null;
		});

		return promise;
	}

	public <T> Cont<R> Await(Cont<CompletableFuture<T>> e, Function<T, Cont<R>> body) {
		return Cont.fromSD((rho, sigma, err) -> e.expressionDenotation.accept(f -> {
			f.whenComplete((a, ex) -> {
				if (a == null) {
					err.accept(ex);
				} else {
					body.apply(a).statementDenotation.accept(rho, sigma, err);
				}
			});
		} , err));
	}
}
