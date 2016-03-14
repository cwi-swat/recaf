package recaf.demo.direct;

import static recaf.core.expr.EvalJavaHelper.toValue;

import java.util.HashMap;
import java.util.Map;

import recaf.core.direct.IEval;
import recaf.core.expr.EvalJavaHelper;
import recaf.core.full.FullJavaDirect;;

public class Memo<R> implements FullJavaDirect<R> {
	
	private Map<MemoizableCall,R> map = new HashMap<MemoizableCall,R>();
	
	public Memo(){
	}
	
	@Override
	public IEval Invoke(IEval obj, String method, IEval... args) {
		return () -> {
			Object evObj = toValue(obj.eval());
			Object[] evArgs = EvalJavaHelper.evaluateArguments(args);
			MemoizableCall key = new MemoizableCall(evObj, method, evArgs);
			if (!map.containsKey(key))
				map.put(key, (R) FullJavaDirect.super.Invoke(EvalJavaHelper.delayObject(evObj), method, EvalJavaHelper.delayObjects(evArgs)).eval());
			return map.get(key);			
		};
	}

}