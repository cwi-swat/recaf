package recaf.demo.cps;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.EvalJavaStmt;
import recaf.core.cps.SD;

public class Backtrack<R> implements EvalJavaStmt<R>, JavaMethodAlg<List<R>, SD<R>> {

	@Override
	public List<R> Method(SD<R> body) {
		List<R> result = new ArrayList<>();
		body.accept(null, ret -> {
			result.add(ret);
		}, () -> {}, l -> {}, l -> {}, exc -> { throw new RuntimeException(exc); });
		return result;
	}
	
	public <T> SD<R> Choose(ISupply<Iterable<T>> choices, Function<? super T, SD<R>> body) {
		return (label, rho, sigma, contin, brk, err) -> {
			get(choices).accept(iter -> {
				for (T t: iter) {
					body.apply(t).accept(null, rho, sigma, contin, brk, err);
				}
			}, err);;
		};
	}
	
	public SD<R> Guard(ISupply<Boolean> cond) {
		return (label, rho, sigma, contin, brk, err) -> {
			get(cond).accept(b -> {
				if (b) {
					sigma.call();
				}
			}, err);
		};
	}
	
	@Override
	public SD<R> Return() {
		throw new AssertionError("Cannot not return a value when backtracking");
	}
	
	
}
