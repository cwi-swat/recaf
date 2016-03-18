package recaf.paper.expr;

public interface PrintAndEvalManual extends MuExpJava<Pair<IEval, String>> {
	static MuExpJavaBase e = new MuExpJavaBase() { };
	static Print p = new Print() { };
	
	@Override
	default Pair<IEval, String> Lit(Object x) {
		return new Pair<>(e.Lit(x), p.Lit(x));
	}

	@Override
	default Pair<IEval, String> This(Object x) {
		return new Pair<>(e.This(x), p.This(x));
	}

	@Override
	default Pair<IEval, String> Field(Pair<IEval, String> x, String f) {
		return new Pair<>(e.Field(x.fst, f), p.Field(x.snd, f));
	}

	@Override
	default Pair<IEval, String> New(Class<?> c, Pair<IEval, String>... es) {
		IEval[] es2 = new IEval[es.length];
		String[] ss = new String[es.length];
		for (int i = 0; i < es.length; i++) {
			es2[i] = es[i].fst;
			ss[i] = es[i].snd;
		}
		return new Pair<>(e.New(c, es2), p.New(c, ss));
	}

	@Override
	default Pair<IEval, String> Invoke(Pair<IEval, String> x, String m, Pair<IEval, String>... es) {
		IEval[] es2 = new IEval[es.length];
		String[] ss = new String[es.length];
		for (int i = 0; i < es.length; i++) {
			es2[i] = es[i].fst;
			ss[i] = es[i].snd;
		}
		return new Pair<>(e.Invoke(x.fst, m, es2), p.Invoke(x.snd, m, ss));
	}

	@Override
	default Pair<IEval, String> Lambda(Object f) {
		return new Pair<>(e.Lambda(f), p.Lambda(f));
	}

	@Override
	default Pair<IEval, String> Var(String x, Object it) {
		return new Pair<>(e.Var(x, it), p.Var(x, it));
	}

}
