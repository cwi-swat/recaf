package recaf.asynciter;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.K0;
import recaf.core.SD;

/***
 * AsyncIter is a composed extension that represents _pulling_ elements
 * asynchronously.
 */
public class AsyncIterExtension<R> extends AbstractJavaCPS<R> {
	
	public interface AsyncEnumerable<T> extends Iterable<CompletableFuture<T>> {}

	public interface AsyncIterator<T> extends Iterator<CompletableFuture<T>> {}
	
	@SuppressWarnings("serial")
	private static final class Yield extends RuntimeException {
		final Object value;
		final K0 k;

		public Yield(Object value, K0 k) {
			this.value = value;
			this.k = k;
		}
	}

	public AsyncEnumerable<R> Method(SD<R> body) {
		return new AsyncEnumerable<R>() {

			boolean exhausted = false;
			
			CompletableFuture<R> current = null;
			
			CompletableFuture<R> promise = new CompletableFuture<R>();
			
			K0 k = () -> {
					CompletableFuture.supplyAsync(() -> {
						body.accept(r -> {
							promise.complete(r);
							exhausted = true;
						} , () -> {
							promise.complete(null);
							exhausted = true;
						} , exc -> {
							promise.completeExceptionally(exc);
							throw new RuntimeException(exc);
						});
					
					return null;
				});
			};

			@Override
			public AsyncIterator<R> iterator() {
				return new AsyncIterator<R>() {

					@SuppressWarnings("unchecked")
					@Override
					public boolean hasNext() {
						if (exhausted) {
							return false;
						}
						try {
							k.call();
							return false;
						} catch (Yield y) {
							current = (CompletableFuture<R>) y.value;
							k = y.k;
							return true;
						}
					}

					@Override
					public CompletableFuture<R> next() {
						return current;
					}

				};
			}
		};
	}

	@Override
	public SD<R> Return(ED<R> e) {
		throw new AssertionError("Cannot return value from coroutine.");
	}

//	public <U> SD<R> Yield(ED<CompletableFuture<U>> exp) {
//		return (rho, sigma, err) -> {
//			exp.accept(f -> {
//				throw new Yield(f, sigma);
//			} , err);
//		};
//	}
//
//	public <U> SD<R> YieldFrom(ED<Iterable<U>> exp) {
//		return For(exp, e -> Yield(Exp(() -> e)));
//	}
	
	
//	public <T> SD<R> Await(ED<CompletableFuture<T>> e, Function<T, SD<R>> body) {
//		return (rho, sigma, err) -> e.accept(f -> {
//			f.whenComplete((a, ex) -> {
//				if (a == null) {
//					err.accept(ex);
//				}
//				else {
//					body.apply(a).accept(rho,  sigma,  err);
//				}
//			});
//		}, err);
//	}
}
