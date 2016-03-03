package recaf.demo.cps;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.SD;
import recaf.core.cps.StmtJava;
import recaf.core.direct.ISupply;

public class Backtrack<R> implements StmtJava<R>, JavaMethodAlg<List<R>, SD<R>> {

	@Override
	public List<R> Method(SD<R> body) {
		List<R> result = new ArrayList<>();
		try {
			body.accept(null, ret -> {
				result.add(ret);
			}, () -> {}, l -> {}, l -> {}, exc -> { throw new RuntimeException(exc); });
		}
		catch (Fail f) {
			
		}
		return result;
	}
	
	private static final class Fail extends RuntimeException {
		
	}
	
	public <T> SD<R> Choose(ISupply<Iterable<T>> choices, Function<? super T, SD<R>> body) {
		return (label, rho, sigma, contin, brk, err) -> {
			get(choices).accept(iter -> {
				for (T t: iter) {
					try {
						body.apply(t).accept(null, rho, sigma, contin, brk, err);
					}
					catch (Fail f) {
					}
				}
				throw new Fail();
			}, err);;
		};
	}
	
	public SD<R> Guard(ISupply<Boolean> cond) {
		return (label, rho, sigma, contin, brk, err) -> {
			get(cond).accept(b -> {
				if (!b) {
					throw new Fail();
				}
				sigma.call();
			}, err);
		};
	}
	
	@Override
	public SD<R> Return() {
		throw new AssertionError("Cannot not return a value when backtracking");
	}
	
	
}
