package recaf.paper.expr;

import java.lang.reflect.Array;

public interface MuExpJavaZip<E1, E2> extends MuExpJava<Pair<E1, E2>> {

	Class<E1> class1();
	Class<E2> class2();
	
	MuExpJava<E1> alg1();
	MuExpJava<E2> alg2();
	
	default Pair<E1[], E2[]> unzip(Pair<E1, E2>[] es) {
		E1[] e1s = (E1[]) Array.newInstance(class1(), es.length);
		E2[] e2s = (E2[]) Array.newInstance(class2(), es.length);
		for (int i = 0; i < es.length; i++) {
			e1s[i] = es[i].fst;
			e2s[i] = es[i].snd;
		}
		return new Pair<>(e1s, e2s);
	}

	@Override
	default Pair<E1, E2> Lit(Object x) {
		return new Pair<>(alg1().Lit(x), alg2().Lit(x));
	}

	@Override
	default Pair<E1, E2> This(Object x) {
		return new Pair<>(alg1().This(x), alg2().This(x));
	}

	@Override
	default Pair<E1, E2> Field(Pair<E1, E2> x, String f) {
		return new Pair<>(alg1().Field(x.fst, f), alg2().Field(x.snd, f));
	}

	@Override
	@SuppressWarnings(value = { "unchecked" })
	default Pair<E1, E2> New(Class<?> c, Pair<E1, E2>... es) {
		Pair<E1[], E2[]> unzipped = unzip(es);
		return new Pair<>(alg1().New(c, unzipped.fst), alg2().New(c, unzipped.snd));
	}

	@Override
	@SuppressWarnings(value = { "unchecked" })
	default Pair<E1, E2> Invoke(Pair<E1, E2> x, String m, Pair<E1, E2>... es) {
		Pair<E1[], E2[]> unzipped = unzip(es);
		return new Pair<>(alg1().Invoke(x.fst, m, unzipped.fst),
				alg2().Invoke(x.snd, m, unzipped.snd));
	}

	@Override
	default Pair<E1, E2> Lambda(Object f) {
		return new Pair<>(alg1().Lambda(f), alg2().Lambda(f));
	}

	@Override
	default Pair<E1, E2> Var(String x, Object it) {
		return new Pair<>(alg1().Var(x, it), alg2().Var(x, it));
	}

}
