package recaf.paper.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.function.Function;

import org.w3c.dom.Node;

import recaf.core.expr.IRef;
import recaf.paper.expr.MuExpJava;
import recaf.paper.full.MuStmJava;
import recaf.paper.methods.MuJavaMethod;


class Document {
	// Stubs for the browser document
	public Node getElementById(String s) {
		return null;
	}
}

class Console {
	// Stubs for the console.
	void log(Object obj) {
	}
}

public 
//BEGIN_TOJS
class ToJS implements MuStmJava<String, String>, MuExpJava<String>, MuJavaMethod<HTTPResponse, String>
//END_TOJS
 {
	
	public static class Browser {
		public Document document;
		public Console console;
	}

	private HTTPRequest request;

	@Override
	public String Exp(String x) {
		return x;
	}

	@Override
	public <T> String Decl(String x, Function<T, String> s) {
		return "let " + getName(s) + " = " + x + "; "+ s.apply(null);
	}

	@Override
	public <T> String For(String x, Function<T, String> s) {
		return "for (let " + getName(s) + " of " + x + ") " + s.apply(null); 
	}

	private <T> String getName(Function<T, String> s) {
		return getParams(s);
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
		if (x instanceof String) {
			return "'" + x + "'";
		}
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
		return "new " + name + "(" + sepList(es) + ")";
	}

	@Override
	public String Invoke(String x, String m, String... es) {
		return x + "." + m + "(" + sepList(es) + ")";
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
		if (it instanceof IRef<?>) {
			it = ((IRef<?>)it).value();
		}
		if (it instanceof HTTPRequest) {
			this.request = (HTTPRequest) it;
			return "req";
		}
		return x;
	}
	
	// These are not in MuJava, but just to make the example work
	public String Ref(String x, Object it) {
		return Var(x,  ((IRef<?>)it).value());
	}
	
	public String Plus(String x, String y) {
		return "(" + x + " + " + y + ")";
	}
	
	public String ExpStat(String x) {
		return x + ";";
	}
	// End of additional stuff.

	@Override
	public HTTPResponse Method(String s) {
		// create response object containing the JS code function (req) {...} and the
		// serialized form of the request.
		return new HTTPResponse(request, "function (req) {" + s + "}");
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
		//System.out.println(null instanceof Response);
		ToJS js = new ToJS();
		HTTPRequest z = new HTTPRequest();
		HTTPResponse response = js.Method(js.If(js.Lit(true), js.For(js.Var("iter", "iter"), x -> 
		  js.Return(js.Var("x", x))), js.Return(js.Var("z", z))));
		System.out.println(response.getJs());
	}

}
