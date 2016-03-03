package recaf.demo.cps;

import java.util.HashMap;
import java.util.Map;

import recaf.core.ISupply;
import recaf.core.cps.K0;
import recaf.core.cps.NoOp;
import recaf.core.cps.SD;

public class ComeFrom<R> implements NoOp<R> {

	private final static Map<String, K0> ks = new HashMap<>();
	
	public SD<R> ComeFrom(ISupply<String> label) {
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
}
