package recaf.paper.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import recaf.core.cps.K0;
import recaf.paper.stm.MuJavaCPS;
import recaf.paper.stm.SD;

// Only support jumping backwards. 

//BEGIN_GOTO
interface Goto<R> extends MuJavaCPS<R> {
	static Map<String, K0> labeled = new HashMap<>();
	
	default SD<R> Label(Supplier<String> l, SD<R> s) {
		return (r, s0) -> {
			labeled.put(l.get(), () -> s.accept(r, s0));
			s.accept(r, s0);
		};
	}
	
	default SD<R> Go(Supplier<String> l) {
		return (r, s) -> labeled.get(l).call();
	}
}
//END_GOTO