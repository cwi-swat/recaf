package recaf.demo.direct;

import java.util.function.Function;

import recaf.core.direct.IEval;
import recaf.core.direct.NoOp;

public class Security<R> implements NoOp<R> {
	
	private Function<Object, Object> filter = null;
	private String securedMethod = null;

	
	public Security(String method, Function<Object, Object> filter){
		this.filter = filter;
		this.securedMethod = method;
	}
	
	@Override
	public IEval Invoke(IEval obj, String method, IEval... args) {
		return () -> {
			Object r = NoOp.super.Invoke(obj, method, args).eval();
			if (method == securedMethod){
				return filter.apply(r);
			}
			else
				return r;
		};
	}

}