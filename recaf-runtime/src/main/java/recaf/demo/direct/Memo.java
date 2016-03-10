package recaf.demo.direct;

import java.util.HashMap;
import java.util.Map;

import recaf.core.direct.IEval;
import recaf.core.direct.NoOp;
import static recaf.core.EvalJavaHelper.toValue;

public class Memo<I,R> implements NoOp<R> {
	
	private Map<I,R> map = new HashMap<I,R>();
	
	private String memoizedMethod;
	
	public Memo(String memoizedMethod){
		this.memoizedMethod = memoizedMethod;
	}
	
	@Override
	public IEval Invoke(IEval obj, String method, IEval... args) {
		return () -> {
			if (method == memoizedMethod){
				I input = (I) toValue(args[0].eval());
				if (map.containsKey(input))
					return map.get(input);
				else{
					R result = (R) NoOp.super.Invoke(obj, method, args).eval();
					map.put(input, result);
					return result;
				}
			}
			else
				return NoOp.super.Invoke(obj, method, args).eval();
		};
	}

}