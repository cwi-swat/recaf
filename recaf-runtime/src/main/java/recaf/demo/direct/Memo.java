package recaf.demo.direct;

import java.util.HashMap;
import java.util.Map;

import recaf.core.EvalJavaHelper;
import recaf.core.direct.IEval;
import recaf.core.direct.NoOp;
import static recaf.core.EvalJavaHelper.toValue;

public class Memo<R> implements NoOp<R> {
	
	private Map<MemoizableCall,R> map = new HashMap<MemoizableCall,R>();
	
	public Memo(){
	}
	
	@Override
	public IEval Invoke(IEval obj, String method, IEval... args) {
		return () -> {
			Object evObj = obj.eval();
			Object[] evArgs = EvalJavaHelper.evaluateArguments(args);
			MemoizableCall key = new MemoizableCall(evObj, method, evArgs);
			if (!map.containsKey(key))
				map.put(key, (R) NoOp.super.Invoke(EvalJavaHelper.delayArgument(evObj), method, EvalJavaHelper.delayArguments(evArgs)).eval());
			return map.get(key);			
		};
	}

}