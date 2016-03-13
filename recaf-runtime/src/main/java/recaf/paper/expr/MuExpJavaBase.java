package recaf.paper.expr;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

//BEGIN_MUEXPJAVA_IMPL
public interface MuExpJavaBase extends MuExpJava<IEval> {
	@Override
	default IEval Lit(Object x) {
		return () -> x;
	}

	@Override
	default IEval This(Object x) {
		return () -> x;
	}

	@Override
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
	
	@Override
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

	@Override
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
	
	@Override
	default IEval Lambda(Object f) {
		return () -> f;
	}

	@Override
	default IEval Var(String x, Object it) {
		return () -> it;
	}

}
//END_MUEXPJAVA_IMPL