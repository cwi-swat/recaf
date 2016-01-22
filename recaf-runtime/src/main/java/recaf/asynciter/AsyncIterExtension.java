package recaf.asynciter;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

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

	@SuppressWarnings("serial")
	private static final class Yield extends RuntimeException {
		final Object value;
		final K0 k;

		public Yield(Object value, K0 k) {
			this.value = value;
			this.k = k;
		}
	}

	public Iterable<CompletableFuture<R>> Method(SD<R> body) {
		return new Iterable<CompletableFuture<R>>() {

			boolean exhausted = false;
			
			CompletableFuture<R> current = null;
			
			K0 k = () -> {
				body.accept(r -> {
					exhausted = true;
				} , () -> {
					exhausted = true;
				} , exc -> {
					throw new RuntimeException(exc);
				});
			};

			@Override
			public Iterator<CompletableFuture<R>> iterator() {
				return new Iterator<CompletableFuture<R>>() {

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

	public <U> SD<R> Yield(ED<U> exp) {
		return (rho, sigma, err) -> {
			exp.accept(v -> {
				throw new Yield(v, sigma);
			} , err);
		};
	}

	public <U> SD<R> YieldFrom(ED<Iterable<U>> exp) {
		return For(exp, e -> Yield(Exp(() -> e)));
	}
}
