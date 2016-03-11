package recaf.paper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.Function;

import java.lang.reflect.Method;

interface Print extends MuExpJava<String>, MuStmJava<String, String, String> {

	@Override
	default String Method(String s) {
		return s;
	}

	@Override
	default String Exp(String e) {
		return e;
	}

	@Override
	default String Decl(String x, Function<?, String> s) {
		return getTypeAndName(s) + " = " + x + "; " + s.apply(null);
	}

	default String getTypeAndName(Function<?, String> s) {
		for (Method m: s.getClass().getMethods()) {
			if (m.getName().equals("apply")) {
				return m.getReturnType() + " " + m.getParameters()[0].getName();
			}
		}
		return null;
	}

	@Override
	default String For(String x, Function<?, String> s) {
		return "for (" + getTypeAndName(s) + ": " + x + ")" + s.apply(null);
	}

	@Override
	default String If(String c, String s1, String s2) {
		return "if (" + c + ")" + s1 + " else " + s2;
	}

	@Override
	default String Return(String e) {
		return "return " + e + ";";
	}

	@Override
	default String Seq(String s1, String s2) {
		return s1 + s2;
	}

	@Override
	default String Empty() {
		return ";";
	}

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

	default String callClosure(Object f) {
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

	default String getFormals(Object f) {
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
