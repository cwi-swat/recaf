package recaf.paper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

interface IEval {
	Object eval() throws Throwable;
}

class MuExpJavaBase implements MuExpJava<IEval> {

	public IEval Lit(Object x) {
		return () -> x;
	}

	public IEval Mul(IEval l, IEval r) {
		return () -> ((Integer)l.eval()) * ((Integer)r.eval());
	}

	public IEval Plus(IEval l, IEval r) {
		return () -> ((Integer)l.eval()) + ((Integer)r.eval());
	}

	public IEval Eq(IEval l, IEval r) {
		return () -> l.eval() == r.eval();
	}

	public IEval This(Object x) {
		return () -> x;
	}

	public IEval Field(IEval x, String f) {
		return () -> {
			Object o = x.eval();
			return o.getClass().getField(f).get(o);
		};
	}
	
	private static Object[] evalArgs(IEval[] es) throws Throwable {
		Object[] args = new Object[es.length];
		for (int i = 0; i < es.length; i++) 
			args[i] = es[i].eval();
		return args;
	}
	
	public IEval New(Class<?> c, IEval... es) {
		return () -> {
			Object[] args = evalArgs(es);
			for (Constructor<?> cons : c.getDeclaredConstructors()) 
				try {
					return cons.newInstance(args);
				} catch (Exception e) {
					continue;
				}
			throw new RuntimeException("could not find constructor");
		};
	}

	public IEval Invoke(IEval x, String m, IEval... es) {
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
			throw new RuntimeException("could not find method " + m);
		};
	}

	public IEval Lambda(Object f) {
		return () -> f;
	}

	public IEval Var(String x, Object it) {
		return () -> it;
	}

}
