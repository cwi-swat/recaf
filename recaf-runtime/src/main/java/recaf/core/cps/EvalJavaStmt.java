package recaf.core.cps;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import recaf.core.Ref;
import recaf.core.alg.JavaStmtAlg;
import recaf.core.direct.ISupply;

public interface EvalJavaStmt<R, E> extends JavaStmtAlg<R, E, SD<R>, CD<R>> {
	
	default <T> BiConsumer<K<T>, K<Throwable>> get(ISupply<T> exp) {
		return (k, err) -> {
			T t = null;
			try {
				t = exp.get();
			} catch (Throwable ex) {
				err.accept(ex);
				return;
			}
			k.accept(t);
		};
	}
	
	@Override
	default SD<R> Empty() {
		return (label, rho, sigma, brk, contin, err) -> sigma.call();
	}

	@Override
	default SD<R> If(ISupply<Boolean> e, SD<R> s) {
		return If(e, s, Empty());
	}

	@Override
	default SD<R> If(ISupply<Boolean> e, SD<R> s1, SD<R> s2) {
		return (label, rho, sigma, brk, contin, err) -> get(e).accept(x -> {
			if (x) {
				s1.accept(label, rho, sigma, brk, contin, err);
			} else {
				s2.accept(label, rho, sigma, brk, contin, err);
			}
		} , err);
	}

	@Override
	default SD<R> Labeled(String label, SD<R> s) {
		return (label0, rho, sigma, brk, contin, err) -> s.accept(label, rho, sigma, brk, contin, err);
	}

	@Override
	default SD<R> While(ISupply<Boolean> e, SD<R> s) {
		return (label, rho, sigma, brk, contin, err) -> {
			new K0() {
				@Override
				public void call() {
					get(e).accept(b -> {
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

	@Override
	default SD<R> DoWhile(SD<R> s, ISupply<Boolean> e) {
		return (label, rho, sigma, brk, contin, err) -> {
			new K0() {

				@Override
				public void call() {
					s.accept(null, rho, () -> {
						get(e).accept(v -> {
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
	@Override
	default <T> SD<R> Switch(ISupply<T> expr, CD<R>... cases) {
		final List<CD<R>> lst = Arrays.asList(cases);

		return (label, rho, sigma, brk, contin, err) -> get(expr).accept(x -> {
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

	@Override
	default <T> CD<R> Case(T constant, SD<R> expStat) {
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
	
	@Override
	default CD<R> Default(SD<R> expStat) {
		return (matched, v, rest, rho, sigma, brk, contin, err) -> {
			if (rest.isEmpty()) {
				// if there was no break, and default is at the end, it's always executed
				expStat.accept(null, rho, sigma, brk, contin, err);
			}
			else {
				// do other cases first, move default handler to end.
				// NB: Java only allows a single default handler for switch-case (phew)
				List<CD<R>> newRest = new LinkedList<>(rest.subList(1, rest.size()));
				
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

	@Override
	default SD<R> Break() {
		return Break(null);
	}

	@Override
	default SD<R> Break(String label) {
		return (label0, rho, sigma, brk, contin, err) -> brk.accept(label);
	}

	@Override
	default SD<R> Continue() {
		return Continue(null);
	}

	@Override
	default SD<R> Continue(String label) {
		return (label0, rho, sigma, brk, contin, err) -> contin.accept(label);
	}

	@Override
	default SD<R> Return() {
		return (label, rho, sigma, brk, contin, err) -> rho.accept(null);
	}

	@Override
	default SD<R> Return(ISupply<R> e) {
		return (label, rho, sigma, brk, contin, err) -> get(e).accept(rho, err);
	}

	
	@Override
	default SD<R> Seq(SD<R> s1, SD<R> s2) {
		return (label, rho, sigma, brk, contin, err) -> s1.accept(label, rho, () -> s2.accept(null, rho, sigma, brk, contin, err), brk, contin, err);
	}

	@Override
	default <T extends Throwable> SD<R> Throw(ISupply<T> e) {
		return (label, rho, sigma, brk, contin, err) -> get(e).accept(r -> err.accept(r), err);
	}

	// TODO: try catch finally.
	@SuppressWarnings("unchecked")
	@Override
	default <T extends Throwable> SD<R> TryCatch(SD<R> body, Class<T> type, Function<T, SD<R>> handle) {
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

	@Override
	default SD<R> TryFinally(SD<R> body, SD<R> fin) {
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


	/*
	 * HOAS for let expressions int x = 3; s ==> Let(Exp(3), x -> [[s]]) S Let(E
	 * exp, Function<E, S> body);
	 */
	@Override
	default <U> SD<R> Decl(ISupply<U> exp, Function<Ref<U>, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> get(exp).accept(r -> body.apply(new Ref<>(r)).accept(label, rho, sigma, brk, contin, err), err);
	}
	
	// For loops
	
	// todo multiple bindings may be introduced (BTW: we cannot do this, need heterogenoeous list).
	// for (int x = 3; cond; update)
	@Override
	default <T> SD<R> ForDecl(ISupply<T> init, Function<Ref<T>, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(init).accept(v -> {
				// NB: we pass in the label of the for loop into the for body.
				body.apply(new Ref<>(v)).accept(label, rho, sigma, brk, contin, err);
			}, err);
		};
	}
	
	@Override
	default SD<R> ForBody(ISupply<Boolean> cond, SD<R> update, SD<R> body) {
		return For(Empty(), cond, update, body);
	}
	
	// NB: technically init/update is not a statement, (so it can't return)
	// but we model it using SD<R> for simplicity's sake.
	// for (x = 3; cond; update)
	
	@Override
	default SD<R> For(SD<R> init, ISupply<Boolean> cond, SD<R> update, SD<R> body) {
		// incorrect with break and continue!
		//return While(cond, Seq2(body, update));
		
		return (label, rho, sigma, brk, contin, err) -> {
			new K0() {

				@Override
				public void call() {
					get(cond).accept(b -> {
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

	@Override
	default <U> SD<R> ForEach(ISupply<Iterable<U>> coll, Function<Ref<U>, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(coll).accept(i -> {
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
