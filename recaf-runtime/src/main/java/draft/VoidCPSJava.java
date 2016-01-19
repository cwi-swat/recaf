package draft;
import static java.util.Arrays.asList;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import builders.core.ED;
import builders.core.K0;
import builders.core.SD;









/*
 * Notes
 * - desugar T x = v1, y = v2 to T x = v1; T y = v2; etc.
 * - desugar: for (s1; c; s2) s3 to { s1; for(; c; s2) }
 * - wrap vars in [] to allow assignment (?)
 * 
 */

public class VoidCPSJava<R> {
	public <T> ED<T> Exp(Supplier<T> e) {
		return (k, err) -> {
			T t = null;
			try {
				t = e.get();
			}
			catch (Throwable ex) {
				err.accept(ex);
				return;
			}
			k.accept(t);
		};
	}
	
	// so this needs type directed translation...
	// because the EDs have same erasure
	
	/*
	 * 
	 * 1 + 2 + await!(f())
	 * 
	 * (k, err) -> Call(() -> f()).accept(fut -> alg.Await(fut).accept(v -> k.accept(1 + 2 + v)))
	 * 
	 * 
	 */
	public ED<Integer> AddII(ED<Integer> l, ED<Integer> r) {
		return (k, err) -> l.accept(v1 -> r.accept(v2 -> k.accept(v1 + v2), err), err);
	}
	
	public ED<Double> AddID(ED<Integer> l, ED<Double> r) {
		return (k, err) -> l.accept(v1 -> r.accept(v2 -> k.accept(v1 + v2), err), err);
	}

	public ED<Double> AddDD(ED<Double> l, ED<Double> r) {
		return (k, err) -> l.accept(v1 -> r.accept(v2 -> k.accept(v1 + v2), err), err);
	}

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
	
	public <T> ED<T> Await(ED<CompletableFuture<T>> e) {
		return (rho, err) -> e.accept(f -> {
			f.whenComplete((a, ex) -> {
				rho.accept(a);
				err.accept(ex);
			});
		}, err);
	}
	
	public <T extends Throwable> SD<R> TryCatch(SD<R> body, Class<T> type, Function<T, SD<R>> handle) {
		return (rho, sigma, err) -> {
			body.accept(rho, sigma, (Throwable exc) -> {
				if (type.isInstance(exc)) {
					handle.apply((T) exc).accept(rho, sigma, err);
				}
				else {
					err.accept(exc);
				}
			});
		};
	}
	
	public SD<R> TryFinally(SD<R> body, SD<R> fin) {
		return (rho, sigma, err) -> {
			body.accept(r -> {
				fin.accept(rho, () -> rho.accept(r), err);
			}, () -> {
				fin.accept(rho, sigma, err);
			}, (Throwable exc) -> {
				fin.accept(rho /* todo: exception here too */, () -> err.accept(exc), err);
			});
		};
	}
	
	public SD<R> Empty() {
		return (rho, sigma, err) -> sigma.call();
	}
	
	public SD<R> IfThen(ED<Boolean> e, SD<R> s) {
		return IfThenElse(e, s, Empty());
	}
	
	public SD<R> IfThenElse(ED<Boolean> e, SD<R> s1, SD<R> s2) {
		return (rho, sigma, err) -> e.accept(x -> {
			if (x) {
				s1.accept(rho, sigma, err);
			}
			else {
				s2.accept(rho, sigma, err);
			}
		}, err); 
	}
	
	/// STAGING
	public SD<R> IfThen(Boolean b, SD<R> s1) {
		return IfThenElse(b, s1, Empty());
	}

	public SD<R> IfThenElse(Boolean b, SD<R> s1, SD<R> s2) {
		return b ? s1 : s2;
	}
	
	public <U> SD<R> ForEach(Iterable<U> coll, Function<U, SD<R>> body) {
		List<SD<R>> lst = new ArrayList<>();
		for (U u: coll) {
			lst.add(body.apply(u));
		}
		return Seq(lst);
	}
	
	public <U> SD<R> Let(U exp, Function<U, SD<R>> body) {
		return body.apply(exp);
	}
	/////// end of staging methods
	
	
	public SD<R> While(ED<Boolean> e, SD<R> s) {
		return (rho, sigma, err) -> {
			new K0() {
				public void call() {
					IfThen(e, Seq2(s, (a, b, c) -> call())).accept(rho, sigma, err);;
				}
			}.call();
		};
	}

	private SD<R> Seq2(SD<R> s1, SD<R> s2) {
		return (rho, sigma, err) -> s1.accept(rho, () -> s2.accept(rho, sigma, err), err);
	}
	
	public SD<R> Seq(List<SD<R>> ss) {
		assert ss.size() > 0;
		return ss.stream().reduce(this::Seq2).get();
	}
	
	public SD<R> Return(ED<R> e) {
		return (rho, sigma, err) -> e.accept(rho, err);
	}
	
	public <T extends Throwable> SD<R> Throw(ED<T> e) {
		return (rho, sigma, err) -> e.accept(r -> err.accept(r), err);
	}

	public <U> SD<R> ExpStat(ED<U> e) {
		return (rho, sigma, err) -> e.accept(ignored -> sigma.call(), err);
	}
	
	// HOAS
	// int x = 3; s ==> Let(Exp(3), x -> [[s]])
	// S Let(E exp, Function<E, S> body);
	<U> SD<R> Let(ED<U> exp, Function<U, SD<R>> body) {
		return (rho, sigma, err) -> exp.accept(r -> body.apply(r).accept(rho, sigma, err), err);
	}
	
	//<U> SD<R> ForEach(ED<Future<U>> coll, Function<U, SD<R>> body) {
	//<U> SD<R> ForEach(ED<Optional<U>> coll, Function<U, SD<R>> body) {
	//<U> SD<R> ForEach(ED<Closeable> coll, Function<U, SD<R>> body) {
	<U> SD<R> ForEach(ED<Iterable<U>> coll, Function<U, SD<R>> body) {
		return (rho, sigma, err) -> coll.accept(iterable -> {
			Iterator<U> iter = iterable.iterator();
			While((s, err2) -> s.accept(iter.hasNext()), 
					Let((s, err2) -> s.accept(iter.next()), body))
			.accept(rho, sigma, err);
		}, err);
	}
	
	SD<R> Until(ED<Boolean> cond, SD<R> body) {
		return Seq2(body, While((k, err) -> cond.accept(b -> k.accept(!b), err), body));
	}
	
	SD<R> Loop(SD<R> body) {
		return While(Exp(() -> true), body);
	}
	
	SD<R> Using(ED<Closeable> resource, Function<Closeable, SD<R>> body) {
		return (rho, sigma, err) -> {
			resource.accept(t -> {
				body.apply(t).accept(r -> {
					try {
						t.close();
					} catch (IOException e) {
						err.accept(e);
					}
					rho.accept(r);
				}, () -> {
					try {
						t.close();
					} catch (IOException e) {
						err.accept(e);
					}
					sigma.call();
				}, exc -> {
					try {
						t.close();
					} catch (IOException e) {
						err.accept(e);
					}
					err.accept(exc);
				});
			}, err);
		};
	}
	
	static void println(String x) {
		System.out.println(x);
	}
	
	static CompletableFuture<Iterable<Integer>> asyncList() {
		return CompletableFuture.supplyAsync(() -> { 
			System.out.println("asList"); 
			return asList(1,2,3,4); });
		
	}
	
	static Integer exampleAsync(VoidCPSJava<Integer> alg, String z) throws InterruptedException, ExecutionException {
		return alg.Method(alg.Seq(asList(alg.<String> Let(alg.Exp(() -> "Hello"),
				x -> alg.<String> Let(alg.Exp(() -> "World!"),
						y -> alg.<Iterable<Integer>> Await(alg.Exp(() -> asyncList()),
								al -> alg.Seq(asList(alg.ExpStat(alg.Exp(() -> {
									System.out.println("bla");
									return null;
								})),
									alg.Return(alg.Exp(() -> 23))	
									
										)))))))).get();
	}
	
 	
	static SD<Integer> example(VoidCPSJava<Integer> alg, String z) {
		return alg.Seq(asList(alg.<String> Let(alg.Exp(() -> "Hello"),
				x -> alg.<String> Let(alg.Exp(() -> "World!"),
						y -> alg.<Future<Iterable<Integer>>> Let(alg.Exp(() -> asyncList()),
								al -> alg.Seq(asList(alg.ExpStat(alg.Exp(() -> {
									System.out.println("bla");
									return null;
								})))))))));
		// alg.<Iterable<Integer>>Await(alg.Exp(() -> al),
		// alg.<Integer>ForEach(alg.Exp(() -> l), i ->
		// alg.Seq(asList(
		// alg.ExpStat(alg.Exp(() -> {println(x + " " + y + ": " + i); return
		// null; })),
		// //alg.Throw(alg.Exp(() -> new Exception())),
		// alg.IfThenElse(alg.Exp(() -> z.length() > 0),
		// alg.ExpStat(alg.Exp(() -> {println("yes"); return null;})),
		// alg.ExpStat(alg.Exp(() -> {println("no"); return null;})))))))))))),
		// alg.Return(alg.<Integer>Exp(() -> 42);
	}
	
	static SD<Integer> exampleFuture(VoidCPSJava<Integer> alg, String z) {
		throw new UnsupportedOperationException();
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		SD<Integer> f = example(new VoidCPSJava<Integer>(), "abc");
//		f.accept(r -> System.out.println("Returned " + r), () -> System.out.println("No return"), e -> System.out.println("Exception: " + e));

		Integer f2 = exampleAsync(new VoidCPSJava<Integer>(), "abc");
		System.out.println(f2);
	}
	
//	static void bla() {
//		for (@Await Integer x: Arrays.asList(1, 2, 3, 4)) {
//			System.out.println(x);
//		}
//	}	
}
//class Backtracking<R> extends VoidCPSJava<R> {
//	@SuppressWarnings("serial")
//	private static class Fail extends RuntimeException {
//	}
//
////	
//	<T> SD<R> Every(ED<Iterable<T>> e, Function<T, SD<R>> body) {
//		return (rho, sigma) -> {
//			e.accept(lst -> {
//				// TODO: need the While encoding here,
//				// sigma is only evaluated if there are no fails during the whole loop. 
//				Iterator<T> iter = lst.iterator();
//				While(s -> s.accept(iter.hasNext()), Let(s -> s.accept(iter.next()), x -> (r1, s1) -> {
//					
//				})).accept(rho, sigma);
//				for (T elt: lst) {
//					try {
//						body.apply(elt).accept(rho, sigma);
//					}
//					catch (Fail f) {
//						continue;
//					}
//				}
//				sigma.call();
//			});
//		};
//	}
//		
//	
//	// Like for loop, but setting choice points with dynamic extent.
//	<T> SD<R> Some(ED<Iterable<T>> e, Function<T, SD<R>> body) {
//		return (rho, sigma) -> {
//			e.accept(lst -> {
//				for (T elt: lst) {
//					try {
//						body.apply(elt).accept(rho, sigma);
//					}
//					catch (Fail f) {
//						continue;
//					}
//				}
//				throw new Fail();
//			});
//		};
//	}
//	
//	SD<R> Fail() {
//		return (rho, sigma) -> { throw new Fail(); };
//	}
//	
//}
