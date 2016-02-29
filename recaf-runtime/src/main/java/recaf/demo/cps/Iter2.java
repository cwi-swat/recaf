//package recaf.demo.cps;
//
//import static recaf.core.direct.EvalJavaHelper.toValue;
//
//import java.util.Iterator;
//import java.util.function.Supplier;
//
//import recaf.core.alg.JavaMethodAlg;
//import recaf.core.cps.FullJava;
//import recaf.core.cps.K0;
//import recaf.core.cps.SD;
//
//public class Iter2<R> implements FullJava<R>, JavaMethodAlg<Iterable<R>, SD<R>> {
//
//	@SuppressWarnings("serial")
//	private static final class Yield extends RuntimeException {
//		Object value;
//		K0 k;
//
//		@Override
//		public synchronized Throwable fillInStackTrace() {
//			return this;
//		}
//	}
//	
//	private static final Yield YIELD = new Yield();
//
//	@Override
//	public Iterable<R> Method(SD<R> body) {
//		return new Iterable<R>() {
//
//			boolean exhausted = false;
//			R current = null;
//			K0 k = () -> {
//				body.accept(null, r -> {
//					exhausted = true;
//				} , () -> {
//					exhausted = true;
//				},  l -> {
//					throw new AssertionError("cannot break without loop");
//				},  l -> {
//					throw new AssertionError("cannot continuewithout loop");
//				} , exc -> {
//					throw new RuntimeException(exc);
//				});
//			};
//
//			@Override
//			public Iterator<R> iterator() {
//				return new Iterator<R>() {
//
//					@SuppressWarnings("unchecked")
//					@Override
//					public boolean hasNext() {
//						if (exhausted) {
//							return false;
//						}
//						try {
//							k.call();
//							return false;
//						} catch (Yield y) {
//							current = (R) y.value;
//							k = y.k;
//							return true;
//						}
//					}
//
//					@Override
//					public R next() {
//						return current;
//					}
//
//				};
//			}
//		};
//	}
//
//	@Override
//	public SD<R> Return(Supplier<R> e) {
//		throw new AssertionError("Cannot return value from iterator.");
//	}
//
//	public <U> SD<R> Yield(Supplier<U> exp) {
//		return (label, rho, sigma, brk, contin, err) -> {
//			get(exp).accept(v -> {
//				YIELD.value = toValue(v);
//				YIELD.k = sigma;
//				throw YIELD;
//			} , err);
//		};
//	}
//
//	public <U> SD<R> YieldFrom(Supplier<Iterable<U>> exp) {
//		return ForEach(exp, e -> Yield(() -> e));
//	}
//}