package recaf.core.cps;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.Ref;

public class AbstractJavaImpl<R> { // implements AbstractJava<R> {
	
	protected R typePreserving(SD<R> body) {
		Ref<R> result = new Ref<>();
		body.accept(null, r -> {
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
	
	
	// this needs to be factored to make the choice of expression
	// representation open. 
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
		return (label, rho, sigma, brk, contin, err) -> sigma.call();
	}

	public SD<R> If(ED<Boolean> e, SD<R> s) {
		return If(e, s, Empty());
	}

	public SD<R> If(ED<Boolean> e, SD<R> s1, SD<R> s2) {
		return (label, rho, sigma, brk, contin, err) -> e.accept(x -> {
			if (x) {
				s1.accept(label, rho, sigma, brk, contin, err);
			} else {
				s2.accept(label, rho, sigma, brk, contin, err);
			}
		} , err);
	}

	public SD<R> Labeled(String label, SD<R> s) {
		return (label0, rho, sigma, brk, contin, err) -> s.accept(label, rho, sigma, brk, contin, err);
	}

	public SD<R> While(ED<Boolean> e, SD<R> s) {
		return (label, rho, sigma, brk, contin, err) -> {
			new K0() {
				@Override
				public void call() {
					e.accept(b -> {
						if (b) {
							s.accept(null, rho, () -> call(),
									l -> {
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
					}, err);
				}
			}.call();
		};
	}

	public SD<R> DoWhile(SD<R> s, ED<Boolean> e) {
		return (label, rho, sigma, brk, contin, err) -> {
			new K0() {

				@Override
				public void call() {
					s.accept(null, rho, () -> {
						e.accept(v -> {
							if (v) {
								call();
							}
							else {
								sigma.call();
							}
						}, err);
					}, l -> {
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
				
			}.call();
		};
	}
	
	@SuppressWarnings("unchecked")
	public <V> SD<R> Switch(ED<V> expr, CD<R, V>... cases) {
		final List<CD<R, V>> lst = Arrays.asList(cases);

		return (label, rho, sigma, brk, contin, err) -> expr.accept(x -> {
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

	public <V> CD<R, V> Case(V constant, SD<R> expStat) {
		return (matched, v, rest, rho, sigma, brk, contin, err) -> {
			if (matched  /* fall through */ || v.equals(constant)) {
				expStat.accept(null, rho, () -> {
					rest.get(0).accept(true, v, rest.subList(1, rest.size()), rho, sigma, brk, contin, err);
				}, brk, contin, err);
			}
			else {
				rest.get(0).accept(false, v, rest.subList(1, rest.size()), rho, sigma, brk, contin, err);
			}
		};
	}
	
	public <V> CD<R, V> Default(SD<R> expStat) {
		return (matched, v, rest, rho, sigma, brk, contin, err) -> {
			if (rest.isEmpty()) {
				// if there was no break, and default is at the end, it's always executed
				expStat.accept(null, rho, sigma, brk, contin, err);
			}
			else {
				// do other cases first, move default handler to end.
				// NB: Java only allows a single default handler for switch-case (phew)
				List<CD<R, V>> newRest = new LinkedList<>(rest.subList(1, rest.size()));
				
				// add a new default handler that's only evaluated if there was no match.
				newRest.add((matched2, v2, rest2, rho2, sigma2, brk2, contin2, err2) -> {
					assert rest2.isEmpty();
					if (!matched2) {
						expStat.accept(null, rho2, () -> {
							// fall through: if default does not break, we get fall through
							// and all subsequent cases should be executed.
							rest.get(0).accept(true, v2, rest.subList(1, rest.size()), rho2, sigma2, brk2, contin2, err2);
						}, brk2, contin2, err2);
					}
					else {
						sigma2.call();
					}
				});
				rest.get(0).accept(matched, v, newRest, rho, sigma, brk, contin, err);
			}
		};
	}

	public SD<R> Break() {
		return Break(null);
	}

	public SD<R> Break(String label) {
		return (label0, rho, sigma, brk, contin, err) -> brk.accept(label);
	}

	public SD<R> Continue() {
		return Continue(null);
	}

	public SD<R> Continue(String label) {
		return (label0, rho, sigma, brk, contin, err) -> contin.accept(label);
	}

	public SD<R> Return() {
		return (label, rho, sigma, brk, contin, err) -> rho.accept(null);
	}

	public SD<R> Return(ED<R> e) {
		return (label, rho, sigma, brk, contin, err) -> e.accept(rho, err);
	}

	
//	public final SD<R> Seq(SD<R>... ss) {
//		assert ss.length > 0;
//		return Stream.of(ss).reduce(this::Seq2).get();
//	}

	public SD<R> Seq(SD<R> s1, SD<R> s2) {
		return (label, rho, sigma, brk, contin, err) -> s1.accept(label, rho, () -> s2.accept(null, rho, sigma, brk, contin, err), brk, contin, err);
	}

	public <T extends Throwable> SD<R> Throw(ED<T> e) {
		return (label, rho, sigma, brk, contin, err) -> e.accept(r -> err.accept(r), err);
	}

	// TODO: try catch finally.
	@SuppressWarnings("unchecked")
	public <T extends Throwable> SD<R> TryCatch(SD<R> body, Class<T> type, Function<T, SD<R>> handle) {
		return (label, rho, sigma, brk, contin, err) -> {
			body.accept(null, rho, sigma, brk, contin, (Throwable exc) -> {
				if (type.isInstance(exc)) {
					handle.apply((T) exc).accept(label, rho, sigma, brk, contin, err);
				} else {
					err.accept(exc);
				}
			});
		};
	}

	public SD<R> TryFinally(SD<R> body, SD<R> fin) {
		return (label, rho, sigma, brk, contin, err) -> {
			body.accept(null, r -> {
				fin.accept(null, rho, () -> rho.accept(r), brk, contin, err);
			} , () -> {
				fin.accept(null, rho, sigma, brk, contin, err);
			} , l -> {
				fin.accept(null, rho, () -> { brk.accept(l); }, brk, contin, err);
			} , l -> {
				fin.accept(null, rho, () -> { contin.accept(l); }, brk, contin, err);
			} , (Throwable exc) -> {
				fin.accept(null, rho /* todo: exception here too??? */, () -> err.accept(exc), brk, contin, err);
			});
		};
	}

	public SD<R> ExpStat(K0 thunk) {
		return (label, rho, sigma, brk, contin, err) -> {
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
	public <U> SD<R> Decl(ED<U> exp, Function<Ref<U>, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> exp.accept(r -> body.apply(new Ref<>(r)).accept(label, rho, sigma, brk, contin, err), err);
	}
	
	// For loops
	
	// todo multiple bindings may be introduced (BTW: we cannot do this, need heterogenoeous list).
	// for (int x = 3; cond; update)
	public <T> SD<R> ForDecl(ED<T> init, Function<Ref<T>, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			init.accept(v -> {
				// NB: we pass in the label of the for loop into the for body.
				body.apply(new Ref<>(v)).accept(label, rho, sigma, brk, contin, err);
			}, err);
		};
	}
	
	public SD<R> ForBody(ED<Boolean> cond, SD<R> update, SD<R> body) {
		return For(Empty(), cond, update, body);
	}
	
	// NB: technically init/update is not a statement, (so it can't return)
	// but we model it using SD<R> for simplicity's sake.
	// for (x = 3; cond; update)
	
	public SD<R> For(SD<R> init, ED<Boolean> cond, SD<R> update, SD<R> body) {
		// incorrect with break and continue!
		//return While(cond, Seq2(body, update));
		
		return (label, rho, sigma, brk, contin, err) -> {
			new K0() {

				@Override
				public void call() {
					cond.accept(b -> {
						if (b) {
							body.accept(null, rho, () -> {
								update.accept(null, rho, () -> {
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
									update.accept(null, rho, () -> {
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

	public <U> SD<R> ForEach(ED<Iterable<U>> coll, Function<Ref<U>, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			coll.accept(i -> {
				Iterator<U> iter = i.iterator(); 
				new K0() {
					@Override
					public void call() {
						if (iter.hasNext()) {
							body.apply(new Ref<>(iter.next())).accept(null, rho, () -> {
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
