package recaf.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import recaf.core.functional.CD;
import recaf.core.functional.ED;
import recaf.core.functional.K;
import recaf.core.functional.K0;
import recaf.core.functional.SD;

public class AbstractJavaImpl<R> { // implements AbstractJava<R> {
	
	protected R typePreserving(SD<R> body) {
		Ref<R> result = new Ref<>();
		body.accept(r -> {
			result.value = r;
		} , () -> {
		} , l -> {
			throw new AssertionError("Cannot call break without loop");
		} , l -> {
			throw new AssertionError("Cannot call continue without loop");
		} , exc -> {
			throw new RuntimeException(exc);
		});
		return result.value;
	}
	
	public <T> ED<T> Exp(Supplier<T> e) {
		return (k, err) -> {
			T t = null;
			try {
				t = e.get();
			} catch (Throwable ex) {
				err.accept(ex);
				return;
			}
			k.accept(t);
		};
	}

	public SD<R> Empty() {
		return (rho, sigma, brk, contin, err) -> sigma.call();
	}

	public SD<R> If(ED<Boolean> e, SD<R> s) {
		return If(e, s, Empty());
	}

	public SD<R> If(ED<Boolean> e, SD<R> s1, SD<R> s2) {
		return (rho, sigma, brk, contin, err) -> e.accept(x -> {
			if (x) {
				s1.accept(rho, sigma, brk, contin, err);
			} else {
				s2.accept(rho, sigma, brk, contin, err);
			}
		} , err);
	}

	public SD<R> Labeled(String label, SD<R> s) {
		return (rho, sigma, brk, contin, err) -> {
			new K0() {
				@Override
				public void call() {
					s.accept(rho, sigma, l -> {
						if (label.equals(l)) {
							sigma.call();
						}
						else {
							brk.accept(l);;
						}
					}, l -> {
						if (label.equals(l)) {
							call(); // !!!
						}
						else {
							contin.accept(l);
						}
					}, err);
				}
				
				
			}.call();
		};
	}

	public SD<R> While(ED<Boolean> e, SD<R> s) {
		return (rho, sigma, brk, contin, err) -> {
			new K0() {
				@Override
				public void call() {
					e.accept(b -> {
						if (b) {
							s.accept(rho, () -> call(),
									l -> {
										if (l == null) {
											sigma.call();
										}
										else {
											brk.accept(l);
										}
									}, l -> {
										if (l == null) {
											call();
										}
										else {
											contin.accept(l);
										}
									}, err);
						}
						else {
							sigma.call();
						}
					}, err);
				}
			}.call();
		};
	}

	public SD<R> DoWhile(SD<R> s, ED<Boolean> e) {
		// wrong because of scoping....
		return Seq2(s, While(e, s));
	}

	/*
	 * TODO: Switch 
	 * - the fields are bad, and should not be needed (e.g. defaultFound, data etc.) 
	 * - break now only works within cases, but it should also work for while/for 
	 *   (use the brk continuation to deal with break) 
	 * - you cannot use foreach over the cases, chain them. 
	 *   Start with
	 *   the first, pass the tail as the normal continuation (e.g., when there’s
	 *   no break). 
	 * - it should also work for Strings, so use Object, not Integer
	 * - data.caseNumber should be avoided by having a ​*different*​ denotation
	 *   for case/defaults
	 */
	
	Stack<SwitchContext<?>> dataCtx = new Stack<>();

	public <V> SD<R> Switch(ED<V> expr, CD<R, V>... cases) {

		List<CD<R, V>> lst = new LinkedList<>();
		lst.addAll(Arrays.asList(cases));
		
		return (rho, sigma, brk, contin, err) -> expr.accept(x -> {
			if (lst.isEmpty()) {
				sigma.call();
			}
			else {
				lst.get(0).accept(false, x, lst.subList(1, lst.size()), rho, sigma, l -> {
					if (l == null) {
						sigma.call();
					}
					else {
						brk.accept(l);
					}
				}, contin, err);
			}
		} , err);
	}

	public <V> CD<R, V> Case(ED<V> constant, SD<R> expStat) {
		return (matched, v, rest, rho, sigma, brk, contin, err) -> {
			constant.accept(r -> {	
				if (matched  /* fall through */ || v.equals(r)) {
					expStat.accept(rho, () -> {
						rest.get(0).accept(true, v, rest.subList(1, rest.size()), rho, sigma, brk, contin, err);
					}, brk, contin, err);
				}
				else {
					rest.get(0).accept(false, v, rest.subList(1, rest.size()), rho, sigma, brk, contin, err);
				}
			} , err);
		};
	}
	
	public <V> CD<R, V> Default(SD<R> expStat) {
		return new CD<R, V>() {
			@Override
			public void accept(boolean matched, V v, List<CD<R, V>> rest, K<R> rho, K0 sigma, K<String> brk, K<String> contin, K<Throwable> err) {
				if (rest.isEmpty()) {
					// if there was no break, and default is at the end, it's always executed
					expStat.accept(rho, sigma, brk, contin, err);
				}
				else {
					// do other cases first, move default handler (= this) up one level.
					List<CD<R, V>> newRest = new LinkedList<>();
					newRest.add(this);
					if (rest.size() > 0) {
						newRest.addAll(rest.subList(1, rest.size()));
					}
					rest.get(0).accept(matched, v, newRest, rho, sigma, brk, contin, err);
				}
			}
			
		};

		//		return (rho, sigma, brk, contin, err) -> {
//			dataCtx.peek().defaultFound = true;
//			if(!dataCtx.peek().caseFound){
//				dataCtx.peek().recordCases.add((SD)expStat);
//			} else
//				expStat.accept(rho, sigma, brk, contin, err);
//
//		};
	}

	public SD<R> Break() {
		return (rho, sigma, brk, contin, err) -> {
//			dataCtx.peek().breakFound = true;
//			brk.accept("");
			brk.accept(null);
		};
	}

	public SD<R> Break(String label) {
		return (rho, sigma, brk, contin, err) -> {
			//dataCtx.peek().breakFound = true;
			brk.accept(label);
		};
	}

	public SD<R> Continue() {
		return (rho, sigma, brk, contin, err) -> contin.accept(null);
	}

	public SD<R> Continue(String label) {
		return (rho, sigma, brk, contin, err) -> contin.accept(label);
	}

	public SD<R> Return(ED<R> e) {
		return (rho, sigma, brk, contin, err) -> e.accept(rho, err);
	}

	public SD<R> Return() {
		return (rho, sigma, brk, contin, err) -> rho.accept(null);
	}

	public final SD<R> Seq(SD<R>... ss) {
		assert ss.length > 0;
		return Stream.of(ss).reduce(this::Seq2).get();
	}

	protected SD<R> Seq2(SD<R> s1, SD<R> s2) {
		return (rho, sigma, brk, contin, err) -> s1.accept(rho, () -> s2.accept(rho, sigma, brk, contin, err), brk, contin, err);
	}

	public <T extends Throwable> SD<R> Throw(ED<T> e) {
		return (rho, sigma, brk, contin, err) -> e.accept(r -> err.accept(r), err);
	}

	public <T extends Throwable> SD<R> TryCatch(SD<R> body, Class<T> type, Function<T, SD<R>> handle) {
		return (rho, sigma, brk, contin, err) -> {
			body.accept(rho, sigma, brk, contin, (Throwable exc) -> {
				if (type.isInstance(exc)) {
					handle.apply((T) exc).accept(rho, sigma, brk, contin, err);
				} else {
					err.accept(exc);
				}
			});
		};
	}

	public SD<R> TryFinally(SD<R> body, SD<R> fin) {
		return (rho, sigma, brk, contin, err) -> {
			body.accept(r -> {
				fin.accept(rho, () -> rho.accept(r), brk, contin, err);
			} , () -> {
			} , (l) -> {
			} , (l) -> {
				fin.accept(rho, sigma, brk, contin, err);
			} , (Throwable exc) -> {
				fin.accept(rho /* todo: exception here too */, () -> err.accept(exc), brk, contin, err);
			});
		};
	}

	public <U> SD<R> ExpStat(K0 thunk) {
		return (rho, sigma, brk, contin, err) -> {
			try {
				thunk.call();
			} catch (Throwable t) {
				err.accept(t);
				return;
			}
			sigma.call();
		};
	}

	/*
	 * HOAS for let expressions int x = 3; s ==> Let(Exp(3), x -> [[s]]) S Let(E
	 * exp, Function<E, S> body);
	 */
	public <U> SD<R> Decl(ED<U> exp, Function<U, SD<R>> body) {
		return (rho, sigma, brk, contin, err) -> exp.accept(r -> body.apply(r).accept(rho, sigma, brk, contin, err), err);
	}
	
	// For loops
	//<Id alg>.For(<Expr condcps>, <Expr updatecps>, <Expr bodycps>)`
	
	// label?: for(; cond; update) body;
	// NB: technically update is not a statement, (so it can't return)
	// but we model it using SD<R> for simplicity's sake.
	// label can be null.
	public SD<R> For(@Nullable String label, ED<Boolean> cond, SD<R> update, SD<R> body) {
		// incorrect with break and continue!
		//return While(cond, Seq2(body, update));
		
		return (rho, sigma, brk, contin, err) -> {
			new K0() {

				@Override
				public void call() {
					cond.accept(b -> {
						if (b) {
							body.accept(rho, () -> {
								update.accept(rho, () -> {
									call();
								}, brk, contin, err);
							}, l -> {
								if (l == label) {
									sigma.call();
								}
								else {
									brk.accept(l);
								}
							}, l -> {
								if (l == label) {
									update.accept(rho, () -> {
										call();	
									}, brk, contin, err);
								}
								else {
									contin.accept(l);
								}
							}, err);
						}
						else {
							sigma.call();
						}
					}, err);
				}
				
			}.call();
		};
	}

	public <U> SD<R> For(@Nullable String label, ED<Iterable<U>> coll, Function<Ref<U>, SD<R>> body) {
		return (rho, sigma, brk, contin, err) -> {
			coll.accept(i -> {
				Iterator<U> iter = i.iterator(); 
				new K0() {
					@Override
					public void call() {
						if (iter.hasNext()) {
							body.apply(new Ref<>(iter.next())).accept(rho, () -> {
								call();
							}, l -> {
								if (l == label) {
									sigma.call();
								}
								else {
									brk.accept(l);
								}
							}, l -> {
								if (l == label) {
									call();
								}
								else {
									contin.accept(l);
								}
							}, err);
						}
						else {
							sigma.call();
						}
					}
					
				}.call();				
			}, err);
		};
	}
	

}
