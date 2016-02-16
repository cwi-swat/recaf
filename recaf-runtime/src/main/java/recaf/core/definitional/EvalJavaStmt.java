package recaf.core.definitional;

import java.util.function.Function;

import recaf.core.Ref;

public interface EvalJavaStmt extends JavaStmtAlg<IEval, IExec, ICase> {

	@Override
	default IExec Decl(IEval exp, Function<Ref<?>, IExec> body) {
		return l -> body.apply(new Ref<>(exp.eval())).exec(null);
	}

	@Override
	default IExec For(String label, IEval coll, Function<Ref<?>, IExec> body) {
		return l -> {
			for (Object x: ((Iterable<?>)coll.eval())) {
				try {
					body.apply(new Ref<>(x)).exec(null);
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
	
	@Override
	default IExec For(String label, IEval cond, IExec update, IExec body) {
		return l -> {
			while (true) {
				if ((Boolean) cond.eval()) {
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
	default IExec If(IEval e, IExec s) {
		return If(e, s, l -> {});
	}

	@Override
	default IExec If(IEval e, IExec s1, IExec s2) {
		return l -> {
			if ((Boolean)e.eval()) {
				s1.exec(null);
			}
			else {
				s2.exec(null);
			}
		};
	}

	@Override
	default IExec While(IEval e, IExec s) {
		return l -> {
			while ((Boolean)e.eval()) {
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
	default IExec DoWhile(IExec s, IEval e) {
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
			while ((Boolean)e.eval());
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
	default IExec Return(IEval e) {
		return l -> { throw new Return(e.eval()); };
	}

	@Override
	default IExec Return() {
		return Return(null);
	}

	@Override
	default IExec Throw(IEval e) {
		return l -> { throw ((Throwable)e.eval()); };
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

	@Override
	default IExec TryCatch(IExec body, Class<? extends Throwable> type, Function<Throwable, IExec> handle) {
		return l -> {
			try {
				body.exec(null);
			}
			catch (Throwable t) {
				if (type.isInstance(t)) {
					handle.apply(t).exec(null);
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
	default IExec ExpStat(IEval exp) {
		return l -> { exp.eval(); };
	}
	
	@Override
	default IExec Switch(IEval expr, ICase... cases) {
		return l -> {
			do {
				Object v = expr.eval();
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
	default ICase Case(IEval constant, IExec expStat) {
		return (v, ft) -> {
			Object c = constant.eval();
			if (ft || (v == null && c == null) || v.equals(c)) {
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
