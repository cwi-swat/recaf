package recaf.ql;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;



public class RenderQL implements QL<RQ> {

	private static String getName(Function<?, ?> f) {
		for (java.lang.reflect.Method m: f.getClass().getDeclaredMethods()) {
			if (m.getName().equals("apply")) {
				Parameter p = m.getParameters()[0];
				return p.getName();
			}
		}
		return null;
	}
	
	
	private final Map<String, Object> env;
	
	public RenderQL(Map<String,Object> env) {
		this.env = env;
	}
	
	public RQ Method(RQ q) {
		return q;
	}

	public <T> Supplier<T> Exp(Supplier<T> t) {
		return t;
	}

	@SuppressWarnings("unchecked")
	public <T> RQ Question(Function<T, RQ> let) {
		return () -> {
			String name = getName(let);
			Object value = env.get(name);
			System.out.println(name + ": " + value);
			let.apply((T) value).render();;
		};
	}

	@SuppressWarnings("unchecked")
	public <T> RQ Question(Supplier<T> exp, Function<T, RQ> let) {
		return () -> {
			String name = getName(let);
			Object value = exp.get();
			System.out.println(name + ": " + value);
			let.apply((T) value).render();
		};
	}


	public RQ If(Supplier<Boolean> exp, RQ question) {
		return () -> {
			Boolean v = exp.get();
			if (v != null && v) {
				question.render();
			}
		};
	}


	public RQ Empty() {
		return () -> {};
	}

}
