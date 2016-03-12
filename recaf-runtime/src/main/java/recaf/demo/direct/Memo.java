package recaf.demo.direct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import recaf.core.direct.IEval;
import recaf.core.full.FullJavaDirect;

import static recaf.core.expr.EvalJavaHelper.toValue;

public class Memo<R> implements FullJavaDirect<R> {
	
	private Map<Object,Object> memo = new HashMap<>();
	
	@Override
	public IEval Invoke(IEval x, String m, IEval... es) {
		// NB: (I)Refs should hash/equals on their contained value.
		return () -> {
			List<Object> key = new ArrayList<>(es.length + 2);
			key.add(toValue(x.eval()));
			key.add(m);
			for (int i = 0; i < es.length; i++) 
				key.add(toValue(es[i].eval()));
			if (!memo.containsKey(key)) {
				IEval[] es2 = Arrays.copyOf(es, es.length);
				IntStream.range(0, es.length).forEach(i -> {
					es2[i] = () -> key.get(i + 2);	
				});
				Object val = FullJavaDirect.super.Invoke(() -> key.get(0), m, es2).eval();
				memo.put(key, val);
			}
			else {
				System.out.println("Memoized call: " + key);
			}
			return memo.get(key);
		};
	}
}