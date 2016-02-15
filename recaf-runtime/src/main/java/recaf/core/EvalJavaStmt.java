package recaf.core;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class EvalJavaStmt<R> {

	protected static class Return extends RuntimeException {
		private final Object result;
		private boolean isVoid = false;

		public Return(Object result) {
			this.result = result;
		}
		
		public Return() {
			this(null);
			this.isVoid  = true;
		}
		
		public Object getResult() {
			return result;
		}
		
		public boolean isVoid() {
			return isVoid;
		}

	}

	@SuppressWarnings("serial")
	private static abstract class Jump extends RuntimeException {
		private final String label;
		protected Jump(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return label;
		}
	}
	
	@SuppressWarnings("serial")
	protected static class Break extends Jump {
		protected Break(String label) {
			super(label);
		}
	}
	
	@SuppressWarnings("serial")
	protected static class Continue extends Jump {
		Continue(String label) {
			super(label);
		}
	}
	
	public <T> Supplier<T> Exp(Supplier<T> e) {
		return e;
	}

	public Stm Empty() {
		return sigma -> sigma.call();
	}

	public Stm If(Supplier<Boolean> e, Stm s) {
		return If(e, s, Empty());
	}

	public Stm If(Supplier<Boolean> e, Stm s1, Stm s2) {
		return sigma -> {
			if (e.get()) {
				s1.accept(sigma);
			} else {
				s2.accept(sigma);
			}
		};
	}

	public Stm Labeled(String label, Stm s) {
		return sigma -> {
			try {
				s.accept(sigma);
			}
			catch (Break b) {
				if (label.equals(b.getLabel())) {
					sigma.call();
				}
				else {
					throw b;
				}
			}
			catch (Continue c) {
				if (label.equals(c.getLabel())) {
					s.accept(sigma);
				}
				else {
					throw c;
				}
			}
			
		};
	}

	public Stm While(Supplier<Boolean> e, Stm s) {
		return sigma -> {
			new K() {
				@Override
				public void call() throws Throwable {
					try {
						If(e, Seq2(s, k -> call())).accept(sigma);
					}
					catch (Break b) {
						sigma.call();
					}
					catch (Continue c) {
						call();
					}
				}
			}.call();
		};
	}

	public Stm DoWhile(Stm s, Supplier<Boolean> e) {
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
	
//	Stack<SwitchContext<?>> dataCtx = new Stack<>();
//
//	public <S> SD<S> Switch(ED<Integer> expr, SD<S>... cases) {
//
//		return sigma -> expr.accept(x -> {
//			dataCtx.push(new SwitchContext<String>());
//
//			dataCtx.peek().caseNumber = x;
//
//			Stream.of(cases).forEach(_case -> {
//				if (!dataCtx.peek().breakFound)
//					_case.accept(rho, sigma, brk, contin, err);
//			});
//			
//			if(!dataCtx.peek().caseFound){
//				dataCtx.peek().recordCases.forEach(_case -> {
//					if (!dataCtx.peek().breakFound)
//						_case.accept((K) rho, sigma, brk, contin, err);
//				});
//			}
//			
//			dataCtx.pop();
//		} , err);
//	}
//
//	public Stm Case(ED<Integer> constant, Stm expStat) {
//		return sigma -> {
//			constant.accept(r -> {	
//				if(dataCtx.peek().defaultFound) {
//					dataCtx.peek().recordCases.add((SD)expStat);
//				}
//				if (r.equals(dataCtx.peek().caseNumber)) {
//					dataCtx.peek().caseFound = true;
//				}
//				if (dataCtx.peek().caseFound) {
//					expStat.accept(rho, sigma, brk, contin, err);
//				}
//			} , err);
//		};
//	}
//
//	public Stm Default(Stm expStat) {
//		return sigma -> {
//			dataCtx.peek().defaultFound = true;
//			if(!dataCtx.peek().caseFound){
//				dataCtx.peek().recordCases.add((SD)expStat);
//			} else
//				expStat.accept(rho, sigma, brk, contin, err);
//
//		};
//	}

	public Stm Break() {
		return sigma -> { throw new Break(null); };
	}

	public Stm Break(String label) {
		return sigma -> { throw new Break(label); };
	}

	public Stm Continue() {
		return sigma -> { throw new Continue(null); };
	}

	public Stm Continue(String label) {
		return sigma -> { throw new Continue(label); };
	}

	public Stm Return(Supplier<R> e) {
		return sigma -> { 
			throw new Return(e.get()); 
		};
	}

	public Stm Return() {
		return sigma -> { throw new Return(); };
	}

	public Stm Seq(Stm... ss) {
		assert ss.length > 0;
		return Stream.of(ss).reduce(this::Seq2).get();
	}

	protected Stm Seq2(Stm s1, Stm s2) {
		return sigma -> s1.accept(() -> s2.accept(sigma));
	}

	public <T extends Throwable> Stm Throw(Supplier<T> e)  {
		return sigma -> { throw e.get(); };
	}

	@SuppressWarnings("unchecked")
	public <T extends Throwable> Stm TryCatch(Stm body, Class<T> type, Function<T, Stm> handle) {
		return sigma -> {
			try {
				body.accept(sigma);
			}
			catch (Throwable t) {
				if (type.isInstance(t)) {
					handle.apply((T) t).accept(sigma);
				}
				else {
					throw t;
				}
			}
		};
	}

	public Stm TryFinally(Stm body, Stm fin) {
		return sigma -> {
			try {
				body.accept(() -> fin.accept(sigma));
			}
			catch (Throwable t) {
				fin.accept(() -> { throw t; });
			}
		};
	}

	public Stm ExpStat(K thunk) {
		return sigma -> { thunk.call(); sigma.call(); };
	}

	/*
	 * HOAS for let expressions int x = 3; s ==> Let(Exp(3), x -> [[s]]) S Let(E
	 * exp, Function<E, S> body);
	 */
	public <T> Stm Decl(Supplier<T> exp, Function<T, Stm> body) {
		return sigma -> body.apply(exp.get()).accept(sigma);
	}
	
	// For loops
	//<Id alg>.For(<Expr condcps>, <Expr updatecps>, <Expr bodycps>)`
	
	// for(; cond; update) body;
	// NB: technically update is not a statement, (so it can't return)
	// but we model it using Stm for simplicity's sake.
	public Stm For(Supplier<Boolean> cond, Stm update, Stm body) {
		return While(cond, Seq2(body, update));
	}

	
	// For each loops (labels?)
	public <T> Stm For(Supplier<Iterable<T>> coll, Function<Ref<T>, Stm> body) {
		return sigma -> {
			Iterator<T> iter = coll.get().iterator();
			do {
				try {
					While(() -> iter.hasNext(), Decl(() -> new Ref<>(iter.next()), body)).accept(sigma);
				}
				catch (Break b) {
					sigma.call();
				}
				catch (Continue c) {
					continue;
				}
			} while (false);
		};
	}
	
	public <T> Stm For(String label, Supplier<Iterable<T>> coll, Function<Ref<T>, Stm> body) {
		return sigma -> {
			Iterator<T> iter = coll.get().iterator();
			do {
				try {
					While(() -> iter.hasNext(), Decl(() -> new Ref<>(iter.next()), body)).accept(sigma);
				}
				catch (Break b) {
					if (label.equals(b.getLabel())) {
						sigma.call();
					}
					else {
						throw b;
					}
				}
				catch (Continue c) {
					if (label.equals(c.getLabel())) {
						continue;
					}
					throw c;
				}
			} while (false);
		};
	}
}
