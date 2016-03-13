package recaf.paper.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import recaf.paper.expr.IEval;
import recaf.paper.expr.MuExpJavaBase;

interface Memo extends MuExpJavaBase {
	
	static List<Object> eval(IEval... es) throws Throwable {
		List<Object> lst = new ArrayList<>(es.length);
		for (int i = 0; i < es.length; i++) { 
			lst.add(es[i].eval());
		}
		return lst;
	}
	
	static IEval[] uneval(List<Object> vs) {
		IEval[] es = new IEval[vs.size()];
		IntStream.range(0, vs.size()).forEach(i -> {
			es[i] = () -> vs.get(i + 2);	
		});
		return es;
	}
	
	//BEGIN_MEMO
	static Map<List<Object>, Object> memo = new HashMap<>();
	
	default IEval Invoke(IEval x, String m, IEval... es) {
		return () -> {
			List<Object> key = new ArrayList<>();
			key.add(x.eval()); key.add(m);
			List<Object> vs = eval(es);
			key.addAll(vs);
			if (!memo.containsKey(key)) {
				Object val = MuExpJavaBase.super
				  .Invoke(() -> key.get(0), m, uneval(vs))
				  .eval();
				memo.put(key, val);
			}
			return memo.get(key);
		};
	}
	//END_MEMO
}
