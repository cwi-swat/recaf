package recaf.core.definitional;

import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.Ref;
import recaf.core.functional.K0;

public interface EvalJavaStmt<E> extends JavaStmtAlg<E, IExec, ICase> {
	
	@Override
	default <T> IExec Decl(Supplier<T> exp, Function<Ref<T>, IExec> body) {
		return l -> body.apply(new Ref<T>(exp.get())).exec(null);
	}

	@Override
	default <T> IExec For(String label, Supplier<Iterable<T>> coll, Function<Ref<T>, IExec> body) {
		return l -> {
			for (T x: coll.get()) {
				try {
					body.apply(new Ref<T>(x)).exec(null);
				}
				catch (Break b) {
					if (b.hasLabel(label)) {
						break;
					}
					throw b;
				}
				catch (Continue c) {
					if (c.hasLabel(label)) {
						continue;
					}
					throw c;
				}
			}
		};
	}
	
	// TODO: make consistent, allow init and localvardec.
	@Override
	default IExec For(String label, Supplier<Boolean> cond, IExec update, IExec body) {
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
	default IExec If(Supplier<Boolean> e, IExec s) {
		return If(e, s, l -> {});
	}

	@Override
	default IExec If(Supplier<Boolean> e, IExec s1, IExec s2) {
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
	default IExec While(Supplier<Boolean> e, IExec s) {
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
	default IExec DoWhile(IExec s, Supplier<Boolean> e) {
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
	default <T> IExec Return(Supplier<T> e) {
		return l -> { throw new Return(e.get()); };
	}

	@Override
	default IExec Return() {
		return Return(null);
	}

	@Override
	default <T extends Throwable> IExec Throw(Supplier<T> e) {
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
	default IExec ExpStat(K0 exp) {
		return l -> { exp.call(); };
	}
	
	@Override
	default <T> IExec Switch(Supplier<T> expr, ICase... cases) {
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
}
