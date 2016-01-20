package recaf.amb;

import java.util.Iterator;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.K0;
import recaf.core.SD;

public class BacktrackingExtension<R> extends AbstractJavaCPS<R> {
	
	@SuppressWarnings("serial")
	private static class Return extends RuntimeException {
		private Object value;
		private K0 k;

		Return(Object value, K0 k) {
			this.value = value;
			this.k = k;
		}
	}
	
	@SuppressWarnings("serial")
	public static class Fail extends RuntimeException {
	}
	
	public Results<R> Method(SD<R> body) {
		Results<R> ret = new Results<R>() {
			K0 k = () -> body.accept(v -> {}, () -> {}, exc -> { throw new RuntimeException(exc); });
			@Override
			public Iterator<R> iterator() {
				return new Iterator<R>() {
					
					@SuppressWarnings("unchecked")
					@Override
					public R next() {
						try {
							System.out.println("In next: " + k);
							K0 mine = k;
							if (mine != null) {
								k = null;
								mine.call();	
							}
						}
						catch (Return e) {
							k = (K0) e.k;
							System.out.println("Got value: " + e.value);
							System.out.println("K = " + e.k);
							return (R) e.value;
						}
						throw new Fail();
					}

					@Override
					public boolean hasNext() {
						return true;
					}
				};
			}
			
		};
		return ret;
	}
	
	@Override
	public SD<R> Return(ED<R> e) {
		return (rho, sigma, err) -> {
			e.accept(v -> {
				throw new Return(v, sigma);
			}, err);
		};
	}
	
	public SD<R> Fail() {
		return Throw(Exp(() -> { System.out.println("failing"); return new Fail(); }));
	}
		
	public SD<R> Check(ED<Boolean> cond) {
		return If((k, err) -> cond.accept(b -> k.accept(!b), err), Fail());
	}
	
//	
//	public SD<R> Not(SD<R> body) {
//		return TryCatch(Seq2(body, Fail()), Fail.class, v -> Empty());
//	}
//	
	// fails if none of the choices succeed
	public <T> SD<R> Some(ED<Iterable<T>> choices, Function<T,SD<R>> body) {
		return For(choices, v -> TryCatch(body.apply(v), Fail.class, e -> Empty()));
	}
//	
//	// fails if any of the choices fail
//	public <T> SD<R> Every(ED<Iterable<T>> choices, Function<T,SD<R>> body) {
//		return (rho, sigma, err) -> {
//			choices.accept(cs -> {
//				for (T t: cs) {
//					try {
//						body.apply(t).accept(rho, sigma, err);
//					}
//					catch (Fail e) {
//						continue;
//					}
//				}
//				sigma.call();
//			}, err);
//		};
//	}
}
