package recaf.asynciter;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.Ref;

public class AsyncIterExtension<R> {

	
	static class Cont<T, EnclosingT> {

		public static <R, EnclosingT> Cont<R, EnclosingT> fromED(ED<R, EnclosingT> expressionDenotation) {
			return new Cont<R, EnclosingT>(expressionDenotation, null);
		}
		public static <R, EnclosingT> Cont<R, EnclosingT> fromSD(SD<R, EnclosingT> statementDenotation) {
			return new Cont<R, EnclosingT>(null, statementDenotation);
		}
		
		//((StreamSubscription -> Unit) -> (T -> Unit) -> (ex -> Unit) -> Unit)
		ED<T, EnclosingT> expressionDenotation;

		//((StreamSubscription -> Unit) -> (T -> Unit) -> (Unit -> Unit) -> (ex -> Unit) -> Unit)
		SD<T, EnclosingT> statementDenotation;

		public Cont(ED<T, EnclosingT> expressionDenotation, SD<T, EnclosingT> statementDenotation) {
			super();
			this.expressionDenotation = expressionDenotation;
			this.statementDenotation = statementDenotation;
		}
	}

	@SuppressWarnings("serial")
	private static final class Yield extends RuntimeException {
		final K0 k;
		final Object value;

		public Yield(Object value, K0 k) {
			this.value = value;
			this.k = k;
		}
	}

	public Cont<R, ?> Break() {
		return null;
	}

	public Cont<R, ?> Break(String label) {
		return null;
	}

	public Cont<R, ?> Continue() {
		return null;
	}

	public Cont<R, ?> Continue(String label) {
		return null;
	}

	/* HOAS for let expressions
	 * int x = 3; s ==> Let(Exp(3), x -> [[s]])
	 * S Let(E exp, Function<E, S> body);
	 * */
//	public <U> Cont<R> Decl(Cont<U> exp, Function<U, Cont<R>> body) {
//		return Cont.fromSD((rho, sigma, err) -> exp.expressionDenotation
//				.accept(r -> body.apply(r).statementDenotation.accept(rho, sigma, err), err));
//	}
//
//	public Cont<R> DoWhile(Cont<R> s, Cont<Boolean> e) {
//		return Seq2(s, While(e, s));
//	}
//
//	public Cont<R> Empty() {
//		return Cont.fromSD((rho, sigma, err) -> sigma.call());
//	}

	public <T> Cont<T, ?> Exp(Supplier<T> e) {
		return Cont.fromED((xi, k, err) -> {
			T t = null;
			try {
				t = e.get();
			} catch (Throwable ex) {
				err.accept(ex);
				return;
			}
			k.accept(t);
		});
	}

	public <U, Enc> Cont<R, Enc> ExpStat(Cont<U, Enc> e) {
		return Cont.fromSD((xi, rho, sigma, err) -> e.expressionDenotation.accept(xi, ignored -> sigma.call(), err));
	}

//	public <U> Cont<R> For(Cont<Iterable<U>> coll, Function<U, Cont<R>> body) {
//		return Cont.fromSD((rho, sigma, err) -> coll.expressionDenotation.accept(iterable -> {
//			Iterator<U> iter = iterable.iterator();
//			While(Cont.fromED((s, err2) -> s.accept(iter.hasNext())),
//					Decl(Cont.fromED((s, err2) -> s.accept(iter.next())), body)).statementDenotation.accept(rho, sigma,
//							err);
//		} , err));
//	}
//
//	public Cont<R> If(Cont<Boolean> e, Cont<R> s) {
//		return If(e, s, Empty());
//	}
//
//	public Cont<R> If(Cont<Boolean> e, Cont<R> s1, Cont<R> s2) {
//		return Cont.fromSD((rho, sigma, err) -> e.expressionDenotation.accept(x -> {
//			if (x) {
//				s1.statementDenotation.accept(rho, sigma, err);
//			} else {
//				s2.statementDenotation.accept(rho, sigma, err);
//			}
//		} , err));
//	}
//
//	public Cont<R> Labeled(String label, Cont<R> s) {
//		return null;
//	}
	
	public Stream<R> Method(Cont<R, ?> body) {
		
		Ref<StreamController<R>> result = new Ref<>();
		
		result.x = new StreamController<>(
			() -> {
				body.statementDenotation.accept(null, 
						r -> {
							if(result.x.isPaused())
								result.x.setOnResume(() -> result.x.close());
							else
								result.x.close();
						}, 
						() -> {
							if(result.x.isPaused())
								result.x.setOnResume(() -> result.x.close());
							else
								result.x.close();
						}, 
						(ex) -> {
							
						});
			}, //onListen
			() -> {
				
			}, //onPause -- not needed for now
			() -> {
				result.x.getOnResume();
			}  //onResume
		);
		
		return result.x.stream();
	}

//	public Iterable<R> Method(SD<R> body) {
//		return new Iterable<R>() {
//
//			R current = null;
//			boolean exhausted = false;
//			K0 k = () -> {
//				body.accept(r -> {
//					exhausted = true;
//				} , () -> {
//					exhausted = true;
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

//	public Cont<R> Return() {
//		return Cont.fromSD((rho, sigma, err) -> rho.accept(null));
//	}
//
//	public SD<R> Return(ED<R> e) {
//		throw new AssertionError("Cannot return value from coroutine.");
//	}
//
//	@SafeVarargs
//	public final Cont<R> Seq(Cont<R>... ss) {
//		assert ss.length > 0;
//		return Stream.of(ss).reduce(this::Seq2).get();
//	}
//
//	protected Cont<R> Seq2(Cont<R> s1, Cont<R> s2) {
//		return Cont.fromSD((rho, sigma, err) -> s1.statementDenotation.accept(rho,
//				() -> s2.statementDenotation.accept(rho, sigma, err), err));
//	}
//
//	public <T extends Throwable> Cont<R> Throw(Cont<T> e) {
//		return Cont.fromSD((rho, sigma, err) -> e.expressionDenotation.accept(r -> err.accept(r), err));
//	}
//
//	public <T extends Throwable> Cont<R> TryCatch(Cont<R> body, Class<T> type, Function<T, Cont<R>> handle) {
//		return Cont.fromSD((rho, sigma, err) -> {
//			body.statementDenotation.accept(rho, sigma, (Throwable exc) -> {
//				if (type.isInstance(exc)) {
//					handle.apply((T) exc).statementDenotation.accept(rho, sigma, err);
//				} else {
//					err.accept(exc);
//				}
//			});
//		});
//	}
//
//	public Cont<R> TryFinally(Cont<R> body, SD<R> fin) {
//		return Cont.fromSD((rho, sigma, err) -> {
//			body.statementDenotation.accept(r -> {
//				fin.accept(rho, () -> rho.accept(r), err);
//			} , () -> {
//				fin.accept(rho, sigma, err);
//			} , (Throwable exc) -> {
//				fin.accept(rho /* todo: exception here too */, () -> err.accept(exc), err);
//			});
//		});
//	}
//
//	public Cont<R> While(Cont<Boolean> e, Cont<R> s) {
//		return Cont.fromSD((rho, sigma, err) -> {
//			new K0() {
//				@Override
//				public void call() {
//					If(e, Seq2(s, Cont.<R> fromSD((a, b, c) -> call()))).statementDenotation.accept(rho, sigma, err);
//				}
//			}.call();
//		});
//	}
//
//	public <U> Cont<R> Yield(Cont<U> exp) {
//		return Cont.fromSD((rho, sigma, err) -> {
//			exp.expressionDenotation.accept(v -> {
//				throw new Yield(v, sigma);
//			} , err);
//		});
//	}
//
//	public <U> Cont<R> YieldFrom(Cont<Iterable<U>> exp) {
//		return For(exp, e -> Yield(Exp(() -> e)));
//	}

//	public SD<R> Return(ED<R> e) {
//		return (rho, sigma, err) -> e.accept(rho, err);
//	}
	
	public <T, Enc> Cont<R, Enc> Await(Cont<CompletableFuture<T>, Enc> e, Function<T, Cont<R, Enc>> body) {
		return Cont.fromSD((xi, rho, sigma, err) -> e.expressionDenotation.accept(xi, f -> {
			xi.pause();
			f.whenComplete((a, ex) -> {
				xi.resume();
				if (a == null) {
					err.accept(ex);
				} else {
					body.apply(a).statementDenotation.accept(xi, rho, sigma, err);
				}
			});
		} , err));
	}
}
