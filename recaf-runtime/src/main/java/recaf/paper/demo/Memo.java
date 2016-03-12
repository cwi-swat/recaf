package recaf.paper.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import recaf.paper.expr.IEval;
import recaf.paper.expr.MuExpJavaBase;

//BEGIN_MEMO
interface Memo extends MuExpJavaBase {
	static Map<List<Object>, Object> memo = new HashMap<>();
	
	default IEval Invoke(IEval x, String m, IEval... es) {
		return () -> {
			List<Object> key = new ArrayList<>(es.length + 2);
			key.set(0, x.eval());
			key.set(1, m);
			for (int i = 0; i < es.length; i++) 
				key.set(i + 2, es[i].eval());
			if (!memo.containsKey(key)) {
				IEval[] es2 = Arrays.copyOf(es, es.length);
				IntStream.range(0, es.length).forEach(i -> {
					es2[i] = () -> key.get(i + 2);	
				});
				Object val = MuExpJavaBase.super
						     .Invoke(() -> key.get(0), m, es2);
				memo.put(key, val);
			}
			return memo.get(key);
		};
	}
}
//END_MEMO