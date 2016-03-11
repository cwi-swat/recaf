package recaf.paper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


//BEGIN_TRACING
interface Tracing extends MuExpJavaBase {
	default IEval Var(String x, Object v){ 
		return () -> {
			System.out.println(x + " = " + v);
			return MuExpJavaBase.super.Var(x, v).eval();
		};
	}
}
//END_TRACING


//BEGIN_MUEXPJAVA_IMPL
interface IEval { Object eval() throws Throwable; }

interface MuExpJavaBase extends MuExpJava<IEval> {
	default IEval Lit(Object x) {
		return () -> x;
	}

	default IEval Mul(IEval l, IEval r) {
		return () -> ((Integer)l.eval()) * ((Integer)r.eval());
	}

	default IEval Plus(IEval l, IEval r) {
		return () -> ((Integer)l.eval()) + ((Integer)r.eval());
	}

	default IEval Eq(IEval l, IEval r) {
		return () -> l.eval() == r.eval();
	}

	default IEval This(Object x) {
		return () -> x;
	}

	default IEval Field(IEval x, String f) {
		return () -> {
			Object o = x.eval();
			return o.getClass().getField(f).get(o);
		};
	}
	
	static Object[] evalArgs(IEval[] es) throws Throwable {
		Object[] args = new Object[es.length];
		for (int i = 0; i < es.length; i++) 
			args[i] = es[i].eval();
		return args;
	}
	
	default IEval New(Class<?> c, IEval... es) {
		return () -> {
			Object[] args = evalArgs(es);
			for (Constructor<?> cons : c.getDeclaredConstructors()) 
				try {
					return cons.newInstance(args);
				} catch (Exception e) {
					continue;
				}
			throw new RuntimeException("no constructor");
		};
	}

	default IEval Invoke(IEval x, String m, IEval... es) {
		return () -> {
			Object recv = x.eval();
			Object[] args = evalArgs(es);
			for (Method method: recv.getClass().getMethods()) 
				if (m.equals(method.getName())) 
					try {
						return method.invoke(recv, args);
					} catch (Exception e) {
						continue;
					}
			throw new RuntimeException("no such method " + m);
		};
	}

	default IEval Lambda(Object f) {
		return () -> f;
	}

	default IEval Var(String x, Object it) {
		return () -> it;
	}
}
//END_MUEXPJAVA_IMPL