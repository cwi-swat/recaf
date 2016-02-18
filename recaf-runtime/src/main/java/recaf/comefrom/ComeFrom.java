package recaf.comefrom;

import java.util.HashMap;
import java.util.Map;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.cps.ED;
import recaf.core.cps.K0;
import recaf.core.cps.SD;

public class ComeFrom<R> extends AbstractJavaImpl<R> {

	private final static Map<String, K0> ks = new HashMap<>();
	
	public R Method(SD<R> body) {
		return typePreserving(body);
	}

	public SD<R> ComeFrom(ED<String> label) {
		return (label0, rho, sigma, brk, contin, err) -> {
			label.accept(l -> {
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
	
}
