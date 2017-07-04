package recaf.core.direct;

import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.Ref;
import recaf.core.alg.JavaStmtOnlyAlg;
import recaf.core.alg.JavaStmtOnlyNoHOASAlg;

public interface EvalJavaStmtNoHOAS<R> extends JavaStmtOnlyNoHOASAlg<R, IExecEnv, ICaseEnv> {

	@Override
	default IExecEnv ExpStat(ISupply<Void> e) {
		return (l, env) -> { e.get(); };
	}
	
	@Override
	default <T> IExecEnv Decl(String x, ISupply<T> exp, IExecEnv body) {
		return (l, env) -> body.exec(l, new Env(x, new Ref<T>(exp.get()), env));
	}

	@Override
	default <T> IExecEnv ForEach(String x, ISupply<Iterable<T>> coll, IExecEnv body) {
		return (l, env) -> {
			for (T elt: coll.get()) {
				try {
					body.exec(null, new Env(x, new Ref<T>(elt), env));
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
	default IExecEnv For(IExecEnv init, ISupply<Boolean> cond, IExecEnv update, IExecEnv body) {
		return (l, env) -> {
			init.exec(null, env);
			ForBody(cond, update, body).exec(l, env);
		};
	}

	@Override
	default <T> IExecEnv ForDecl(String x, ISupply<T> init, IExecEnv body) {
		return (l, env) -> body.exec(l, new Env(x, new Ref<>(init.get()), env));
	}
	
	@Override
	default IExecEnv ForBody(ISupply<Boolean> cond, IExecEnv update, IExecEnv body) {
		return (l, env) -> {
			while (true) {
				if (cond.get()) {
					try {
						body.exec(null, env);
					}
					catch (Break b) {
						if (b.hasLabel(l)) {
							break;
						}
						throw b;
					}
					catch (Continue c) {
						if (c.hasLabel(l)) {
							update.exec(null, env);
							continue;
						}
						throw c;
					}
					update.exec(null, env);
				}
				else {
					break;
				}
			}
		};
	}

	@Override
	default IExecEnv If(ISupply<Boolean> e, IExecEnv s) {
		return If(e, s, (l, env) -> {});
	}

	@Override
	default IExecEnv If(ISupply<Boolean> e, IExecEnv s1, IExecEnv s2) {
		return (l, env) -> {
			if (e.get()) {
				s1.exec(null, env);
			}
			else {
				s2.exec(null, env);
			}
		};
	}

	@Override
	default IExecEnv While(ISupply<Boolean> e, IExecEnv s) {
		return (l, env) -> {
			while (e.get()) {
				try {
					s.exec(null, env);
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
	default IExecEnv DoWhile(IExecEnv s, ISupply<Boolean> e) {
		return (l, env) -> {
			do {
				try {
					s.exec(null, env);
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
	default IExecEnv Labeled(String label, IExecEnv s) {
		return (l, env) -> s.exec(label, env);
	}

	@Override
	default IExecEnv Break(String label) {
		return (l, env) -> { throw new Break(label); };
	}

	@Override
	default IExecEnv Break() {
		return Break(null);
	}

	@Override
	default IExecEnv Continue(String label) {
		return (l, env) -> { throw new Continue(label); };
	}

	
	@Override
	default IExecEnv Continue() {
		return Continue(null);
	}

	@Override
	default IExecEnv Return(ISupply<R> e) {
		return (l, env) -> { throw new Return(e.get()); };
	}

	@Override
	default IExecEnv Return() {
		return Return(() -> null);
	}

	@Override
	default <T extends Throwable> IExecEnv Throw(ISupply<T> e) {
		return (l, env) -> { throw e.get(); };
	}

	@Override
	default IExecEnv Empty() {
		return (l, env) -> {};
	}

	@Override
	default IExecEnv Seq(IExecEnv s1, IExecEnv s2) {
		return (l, env) -> {
			s1.exec(l, env);
			s2.exec(null, env);
		};
	}

	@Override
	default <T extends Throwable> IExecEnv TryCatch(IExecEnv body, Class<T> type, String x, IExecEnv handle) {
		return (l, env) -> {
			try {
				body.exec(null, env);
			}
			catch (Throwable t) {
				if (type.isInstance(t)) {
					handle.exec(null, new Env(x, t, env));
				}
				else {
					throw t;
				}
			}
		};
	}

	@Override
	default IExecEnv TryFinally(IExecEnv body, IExecEnv fin) {
		return (l, env) -> {
			try {
				body.exec(null, env);
			}
			finally {
				fin.exec(l, env);
			}
		};
	}
	
	@Override
	default <T> IExecEnv Switch(ISupply<T> expr, ICaseEnv... cases) {
		return (l, env) -> {
			do {
				T v = expr.get();
				try {
					boolean fallThrough = false;
					int defaultIndex = -1;
					for (int i = 0; i < cases.length; i++) {
						try {
							fallThrough = cases[i].eval(v, fallThrough, env);
						}
						catch (Default d) {
							defaultIndex = i;
						}
					}
					if (defaultIndex > -1 && !fallThrough) {
						for (int i = defaultIndex; i < cases.length; i++) {
							cases[i].eval(v, true, env);
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
	default <T> ICaseEnv Case(T constant, IExecEnv expStat) {
		return (v, ft, env) -> {
			if (ft || (v == constant) || (v != null && v.equals(constant))) {
				expStat.exec(null, env);
				return true;
			}
			return false;
		};
	}

	
	@Override
	default ICaseEnv Default(IExecEnv expStat) {
		return (v, ft, env) -> {
			if (ft) {
				expStat.exec(null, env);
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

