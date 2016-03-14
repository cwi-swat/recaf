package recaf.paper.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import recaf.paper.expr.MuExpJava;
import recaf.paper.full.MuStmJava;
import recaf.paper.methods.MuJavaMethod;

class Remote {

	private Object self;
	private String code;

	public Remote(Object self, String code) {
		this.self = self;
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}

public class ToJS implements MuStmJava<String, String>, MuExpJava<String>, MuJavaMethod<Remote, String> {

	private Object remote;
	private List<String> classStubs = new ArrayList<>();

	@Override
	public String Exp(String x) {
		return x;
	}

	@Override
	public <T> String Decl(String x, Function<T, String> s) {
		return "let " + x + " = " + s.apply(null);
	}

	@Override
	public <T> String For(String x, Function<T, String> s) {
		return "for (let " + getName(s) + " of " + x + ")" + s.apply(null); 
	}

	private <T> String getName(Function<T, String> s) {
		return "todo";
	}

	@Override
	public String If(String c, String s1, String s2) {
		return "if (" + c + ") " + s1 + " else " + s2;
	}

	@Override
	public String Return(String x) {
		return "return " + x + ";";
	}

	@Override
	public String Seq(String s1, String s2) {
		return s1 + s2;
	}

	@Override
	public String Empty() {
		return ";";
	}
	

	@Override
	public String Lit(Object x) {
		return "" + x;
	}

	@Override
	public String This(Object x) {
		return "self";
	}

	@Override
	public String Field(String x, String f) {
		return x + "." + f;
	}

	@Override
	public String New(Class<?> c, String... es) {
		String name = c.getSimpleName();
		if (!classStubs.contains(name)) {
			classStubs.add(name);
		}
		return "new " + name + "(" + sepList(es) + ")";
	}

	@Override
	public String Invoke(String x, String m, String... es) {
		return x + "." + m + "(" + sepList(es )+ ")";
	}

	@Override
	public String Lambda(Object f) {
		return "function(" + getParams(f) + ") {" + callClosure(f) + "}";
	}

	private String callClosure(Object f) {
		for (java.lang.reflect.Method m: f.getClass().getMethods()) {
			Object args = new Object[m.getParameterCount()];
			try {
				return (String) m.invoke(f, args);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		return "";
	}

	private String getParams(Object f) {
		for (java.lang.reflect.Method m: f.getClass().getMethods()) {
			Object args[] = new Object[m.getParameterCount()];
			int i = 0;
			for (Parameter p: m.getParameters()) {
				args[i] = p.getName();
				i++;
			}
			return sepList(args);
		}
		return "";
	}

	@Override
	public String Var(String x, Object it) {
		return x;
	}

	@Override
	public Remote Method(String s) {
		return new Remote(remote, "function (self, " + sepList(classStubs.toArray()) + ") {" + s + "}");
	}
	
	private String sepList(Object[] os) {
		String s = "";
		for (Object o: os) {
			s += o.toString() + ", ";
		}
		if (!s.isEmpty()) {
			return s.substring(0, s.length() - 2);
		}
		return s;
	}
	
	public static void main(String[] args) {
		ToJS js = new ToJS();
		Remote src = js.Method(js.If(js.Lit(true), js.Return(js.New(Integer.class)), js.Return(js.Lit(null))));
		System.out.println(src.getCode());
	}

}
