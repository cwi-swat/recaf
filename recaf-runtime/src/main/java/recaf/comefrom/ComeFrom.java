package recaf.comefrom;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import recaf.core.cps.EvalJavaStmt;
import recaf.core.cps.K0;
import recaf.core.cps.SD;

public class ComeFrom<R> implements EvalJavaStmt<R, Supplier<?>> {

	private final static Map<String, K0> ks = new HashMap<>();
	
	public R Method(SD<R> body) {
		return typePreserving(body);
	}

	public SD<R> ComeFrom(Supplier<String> label) {
		return (label0, rho, sigma, brk, contin, err) -> {
			get(label).accept(l -> {
				ks.put(l, sigma);
				sigma.call();
			}, err);
		};
	}
	
	@Override
	public SD<R> Labeled(String label, SD<R> s) {
		return (label0, rho, sigma, brk, contin, err) -> {
			if (ks.containsKey(label)) {
				ks.get(label).call();
				return;
			}
			s.accept(label, rho, sigma, brk, contin, err);
		};
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Supplier<T> Exp(Supplier<?> e) {
		return (Supplier<T>) e;
	}
	
}
