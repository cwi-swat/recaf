package recaf.iter;

import java.util.Iterator;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.K0;
import recaf.core.SD;

public class Iter<R> extends AbstractJavaCPS<R> {

	@SuppressWarnings("serial")
	private static final class Yield extends RuntimeException {
		final Object value;
		final K0 k;

		public Yield(Object value, K0 k) {
			this.value = value;
			this.k = k;
		}
	}

	public Iterable<R> Method(SD<R> body) {
		return new Iterable<R>() {
			
			boolean exhausted = false;
			R current = null;
			K0 k = () -> { body.accept(r -> { exhausted = true; },
					  () -> { exhausted = true; }, 
					  exc -> { throw new RuntimeException(exc); }); };
			
			@Override
			public Iterator<R> iterator() {
				return new Iterator<R>() {

					@Override
					public boolean hasNext() {
						if (exhausted) {
							return false;
						}
						try {
							k.call();
							return false;
						}
						catch (Yield y) {
							current = (R) y.value;
							k = y.k;
							return true;
						}
					}

					@Override
					public R next() {
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
			}, err);
		};
	}
	
	public <U> SD<R> YieldFrom(ED<Iterable<U>> exp) {
		return For(exp, e -> Yield(Exp(() -> e)));
	}
}
