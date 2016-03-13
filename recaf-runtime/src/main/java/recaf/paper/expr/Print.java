package recaf.paper.expr;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

interface Print extends MuExpJava<String> {
	@Override
	default String Lit(Object x) {
		return x.toString();
	}

	@Override
	default String This(Object x) {
		return "this";
	}

	@Override
	default String Field(String x, String f) {
		return x + "." + f;
	}

	@Override
	default String New(Class<?> c, String... es) {
		return "new " + c.getName() + "(" + es + ")";
	}

	@Override
	default String Invoke(String x, String m, String... es) {
		return x + "." + m + "(" + es + ")";
	}

	@Override
	default String Lambda(Object f) {
		return "(" + getFormals(f) + ") -> " + callClosure(f);
	}

	static String callClosure(Object f) {
		for (Method m: f.getClass().getDeclaredMethods()) {
			Object[] args = new Object[m.getParameterCount()];
			Arrays.fill(args, null);
			try {
				return (String)m.invoke(f, args);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	static String getFormals(Object f) {
		String formals = "";
		for (Method m: f.getClass().getDeclaredMethods()) {
			for (Parameter p: m.getParameters()) {
				formals += p.getType().getName() + " " + p.getName() + ", ";
			}
			if (!formals.isEmpty()) {
				return formals.substring(0, formals.length() - 2);
			}
			return formals;
		}
		return null;
	}

	@Override
	default String Var(String x, Object it) {
		return x;
	}

}
