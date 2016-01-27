package recaf.asynciter;

import java.util.Iterator;
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
	public <U, Enc> Cont<R, Enc> Decl(Cont<U, Enc> exp, Function<U, Cont<R, Enc>> body) {
		return Cont.fromSD((xi, rho, sigma, err) -> exp.expressionDenotation
				.accept(xi, r -> body.apply(r).statementDenotation.accept(xi, rho, sigma, err), err));
	}

	public <Enc> Cont<R, Enc> DoWhile(Cont<R, Enc> s, Cont<Boolean, Enc> e) {
		return Seq2(s, While(e, s));
	}

	public <Enc> Cont<R, Enc> Empty() {
		return Cont.fromSD((xi, rho, sigma, err) -> sigma.call());
	}

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

	public <U, Enc> Cont<R, Enc> For(Cont<Iterable<U>, Enc> coll, Function<U, Cont<R, Enc>> body) {
		return Cont.fromSD((xi, rho, sigma, err) -> coll.expressionDenotation.accept(xi, iterable -> {
			Iterator<U> iter = iterable.iterator();
			While(Cont.fromED((x, s, err2) -> s.accept(iter.hasNext())),
					Decl(Cont.fromED((x, s, err2) -> s.accept(iter.next())), body)).statementDenotation.accept(xi, rho,
							sigma, err);
		} , err));
	}

	public <Enc> Cont<R, Enc> If(Cont<Boolean, Enc> e, Cont<R, Enc> s) {
		return If(e, s, Empty());
	}

	public <Enc> Cont<R, Enc> If(Cont<Boolean, Enc> e, Cont<R, Enc> s1, Cont<R, Enc> s2) {
		return Cont.fromSD((xi, rho, sigma, err) -> e.expressionDenotation.accept(xi, x -> {
			if (x) {
				s1.statementDenotation.accept(xi, rho, sigma, err);
			} else {
				s2.statementDenotation.accept(xi, rho, sigma, err);
			}
		} , err));
	}

	public <Enc> Cont<R, Enc> Labeled(String label, Cont<R, Enc> s) {
		return null;
	}

	// This should be appeared spliced-in per method declaration, right?
	Ref<StreamController<R>> result = new Ref<>();

	public Stream<R> Method(Cont<R, ?> body) {
			
		result.value = new StreamController<>(
			() -> {
				body.statementDenotation.accept(null, 
						r -> {
							if(result.value.isPaused())
								result.value.setOnResume(() -> result.value.close());
							else
								result.value.close();
						}, 
						() -> {
							if(result.value.isPaused())
								result.value.setOnResume(() -> result.value.close());
							else
								result.value.close();
						}, 
						(ex) -> {
							
						});
			}, //onListen
			() -> {
				
			}, //onPause -- not needed for now
			() -> {
				result.value.getOnResume();
			}  //onResume
		);
		
		return result.value.stream();
	}

	@SafeVarargs
	public final <Enc> Cont<R,Enc> Seq(Cont<R,Enc>... ss) {
		assert ss.length > 0;
		return java.util.stream.Stream.of(ss).reduce(this::Seq2).get();
	}

	protected <Enc> Cont<R, Enc> Seq2(Cont<R, Enc> s1, Cont<R, Enc> s2) {
		return Cont.fromSD((xi, rho, sigma, err) -> s1.statementDenotation.accept(xi, rho,
				() -> s2.statementDenotation.accept(xi, rho, sigma, err), err));
	}

	public <T extends Throwable, Enc> Cont<R, Enc> Throw(Cont<T, Enc> e) {
		return Cont.fromSD((xi, rho, sigma, err) -> e.expressionDenotation.accept(xi, r -> err.accept(r), err));
	}

	public <T extends Throwable, Enc> Cont<R, Enc> TryCatch(Cont<R, Enc> body, Class<T> type, Function<T, Cont<R, Enc>> handle) {
		return Cont.fromSD((xi, rho, sigma, err) -> {
			body.statementDenotation.accept(xi, rho, sigma, (Throwable exc) -> {
				if (type.isInstance(exc)) {
					handle.apply((T) exc).statementDenotation.accept(xi, rho, sigma, err);
				} else {
					err.accept(exc);
				}
			});
		});
	}

	public <Enc> Cont<R, Enc> TryFinally(Cont<R, Enc> body, Cont<R, Enc> fin) {
		return Cont.fromSD((xi, rho, sigma, err) -> {
			body.statementDenotation.accept(xi, r -> {
				fin.statementDenotation.accept(xi, rho, () -> rho.accept(r), err);
			} , () -> {
				fin.statementDenotation.accept(xi, rho, sigma, err);
			} , (Throwable exc) -> {
				fin.statementDenotation.accept(xi, rho /* todo: exception here too */, () -> err.accept(exc), err);
			});
		});
	}

	public <Enc> Cont<R, Enc> While(Cont<Boolean, Enc> e, Cont<R, Enc> s) {
		return Cont.fromSD((xi, rho, sigma, err) -> {
			new K0() {
				@Override
				public void call() {
					If(e, Seq2(s, Cont.<R, Enc>fromSD((a, b, c, d) -> call()))).statementDenotation.accept(xi, rho, sigma, err);
				}
			}.call();
		});
	}

	public <Enc> Cont<R, Enc> Yield(Cont<R, Enc> exp) {
		return Cont.fromSD((xi, rho, sigma, err) -> {
			exp.expressionDenotation.accept(xi, r -> {
				if (result.value.isPaused()){
					xi.pause();
					
					result.value.setOnResume(() -> {
						xi.resume();
						result.value.add(r);
						sigma.call();
					});
				}
				else {
					result.value.add(r);
					sigma.call();
				}
			} , err);
		});
	}

	public <Enc> Cont<R, Enc> YieldFrom(Cont<Stream<R>, Enc> exp) {
		return Cont.fromSD((xi, rho, sigma, err) -> {
			exp.expressionDenotation.accept(xi, r -> { 
				if (result.value.isPaused()){
					xi.pause();
					result.value.setOnResume(() -> {
						xi.resume();
						result.value.addStream(r);
						sigma.call();
					});
				}
				else {
					result.value.addStream(r);
					sigma.call();
				}
			}, err);
		});
	}

	public <Enc> Cont<R, Enc> Return(Cont<R, Enc> e) {
		return Cont.fromSD(((xi, rho, sigma, err) -> e.expressionDenotation.accept(xi, rho, err)));
	}
	
	public <Enc> Cont<R, Enc> Return() {
		return Cont.fromSD(((xi, rho, sigma, err) -> rho.accept(null)));
	}

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
	
	public <T, Enc> Cont<R, Enc> AwaitFrom(Cont<Stream<T>, Enc> e, Function<T, Cont<R, Enc>> body) {
//		return Cont.fromSD((xi, rho, sigma, err) -> e.expressionDenotation.accept(xi, f -> { 
//			xi.pause();
//			Ref<StreamSubscription> inner_xi = new Ref(); 
//					
//			f.listen(v -> Cont.fromSD(()));
//		}, err));
		throw new UnsupportedOperationException();		
	}

}
