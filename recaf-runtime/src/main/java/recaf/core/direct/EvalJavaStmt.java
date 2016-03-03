package recaf.core.direct;

import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.Ref;
import recaf.core.alg.JavaStmtAlg;

public interface EvalJavaStmt<R, E> extends JavaStmtAlg<R, E, IExec, ICase> {
	
	@Override
	default <T> IExec Decl(ISupply<T> exp, Function<Ref<T>, IExec> body) {
		return l -> body.apply(new Ref<T>(exp.get())).exec(null);
	}

	@Override
	default <T> IExec ForEach(ISupply<Iterable<T>> coll, Function<Ref<T>, IExec> body) {
		return l -> {
			for (T x: coll.get()) {
				try {
					body.apply(new Ref<T>(x)).exec(null);
				}
				catch (Break b) {
					if (b.hasLabel(l)) {
						break;
					}
					throw b;
				}
				catch (Continue c) {
					if (c.hasLabel(l)) {
						continue;
					}
					throw c;
				}
			}
		};
	}

	
	@Override
	default IExec For(IExec init, ISupply<Boolean> cond, IExec update, IExec body) {
		return l -> {
			init.exec(null);
			ForBody(cond, update, body).exec(l);
		};
	}

	@Override
	default <T> IExec ForDecl(ISupply<T> init, Function<Ref<T>, IExec> body) {
		return l -> body.apply(new Ref<>(init.get())).exec(l);
	}
	
	@Override
	default IExec ForBody(ISupply<Boolean> cond, IExec update, IExec body) {
		return l -> {
			while (true) {
				if (cond.get()) {
					try {
						body.exec(null);
					}
					catch (Break b) {
						if (b.hasLabel(l)) {
							break;
						}
						throw b;
					}
					catch (Continue c) {
						if (c.hasLabel(l)) {
							update.exec(null);
							continue;
						}
						throw c;
					}
					update.exec(null);
				}
				else {
					break;
				}
			}
		};
	}

	@Override
	default IExec If(ISupply<Boolean> e, IExec s) {
		return If(e, s, l -> {});
	}

	@Override
	default IExec If(ISupply<Boolean> e, IExec s1, IExec s2) {
		return l -> {
			if (e.get()) {
				s1.exec(null);
			}
			else {
				s2.exec(null);
			}
		};
	}

	@Override
	default IExec While(ISupply<Boolean> e, IExec s) {
		return l -> {
			while (e.get()) {
				try {
					s.exec(null);
				}
				catch (Break b) {
					if (b.hasLabel(l)) {
						break;
					}
					throw b;
				}
			}
		};
	}

	@Override
	default IExec DoWhile(IExec s, ISupply<Boolean> e) {
		return l -> {
			do {
				try {
					s.exec(null);
				}
				catch (Break b) {
					if (b.hasLabel(l)) {
						break;
					}
					throw b;
				}
				catch (Continue c) {
					if (c.hasLabel(l)) {
						continue;
					}
					throw c;
				}
			}
			while (e.get());
		};
	}

	@Override
	default IExec Labeled(String label, IExec s) {
		return l -> s.exec(label);
	}

	@Override
	default IExec Break(String label) {
		return l -> { throw new Break(label); };
	}

	@Override
	default IExec Break() {
		return Break(null);
	}

	@Override
	default IExec Continue(String label) {
		return l -> { throw new Continue(label); };
	}

	
	@Override
	default IExec Continue() {
		return Continue(null);
	}

	@Override
	default IExec Return(ISupply<R> e) {
		return l -> { throw new Return(e.get()); };
	}

	@Override
	default IExec Return() {
		return Return(() -> null);
	}

	@Override
	default <T extends Throwable> IExec Throw(ISupply<T> e) {
		return l -> { throw e.get(); };
	}

	@Override
	default IExec Empty() {
		return l -> {};
	}

	@Override
	default IExec Seq(IExec s1, IExec s2) {
		return l -> {
			s1.exec(l);
			s2.exec(null);
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	default <T extends Throwable> IExec TryCatch(IExec body, Class<T> type, Function<T, IExec> handle) {
		return l -> {
			try {
				body.exec(null);
			}
			catch (Throwable t) {
				if (type.isInstance(t)) {
					handle.apply((T) t).exec(null);
				}
				else {
					throw t;
				}
			}
		};
	}

	@Override
	default IExec TryFinally(IExec body, IExec fin) {
		return l -> {
			try {
				body.exec(null);
			}
			finally {
				fin.exec(l);
			}
		};
	}
	
	@Override
	default <T> IExec Switch(ISupply<T> expr, ICase... cases) {
		return l -> {
			do {
				T v = expr.get();
				try {
					boolean fallThrough = false;
					int defaultIndex = -1;
					for (int i = 0; i < cases.length; i++) {
						try {
							fallThrough = cases[i].eval(v, fallThrough);
						}
						catch (Default d) {
							defaultIndex = i;
						}
					}
					if (defaultIndex > -1 && !fallThrough) {
						for (int i = defaultIndex; i < cases.length; i++) {
							cases[i].eval(v, true);
						}
					}
				}
				catch (Break b) {
					if (b.hasLabel(l)) {
						break;
					}
					throw b;
				}
			} while (false);
		};
	}
	
	@Override
	default <T> ICase Case(T constant, IExec expStat) {
		return (v, ft) -> {
			if (ft || (v == constant) || (v != null && v.equals(constant))) {
				expStat.exec(null);
				return true;
			}
			return false;
		};
	}

	
	@Override
	default ICase Default(IExec expStat) {
		return (v, ft) -> {
			if (ft) {
				expStat.exec(null);
				return true;
			}
			throw new Default();
		};
	}
	
	@SuppressWarnings("serial")
	abstract class Jump extends Exception {
		private final String label;

		public Jump(String label) {
			this.label = label;
		}

		
		public boolean hasLabel(String l) {
			if (l == label) {
				return true;
			}
			if (l != null) {
				return l.equals(label);
			}
			return false;
		}
	}

	@SuppressWarnings("serial")
	final class Break extends Jump {
		public Break(String label) {
			super(label);
		}
	}

	@SuppressWarnings("serial")
	final class Continue extends Jump {

		public Continue(String label) {
			super(label);
		}

	}

	@SuppressWarnings("serial")
	final class Default extends RuntimeException {

	}

	@SuppressWarnings("serial")
	final class Return extends Exception {
		private final Object value;

		public Return(Object value) {
			this.value = value;
		}
		
		public Object getValue() {
			return value;
		}
	}

}

