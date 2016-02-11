package recaf.backtrack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class Backtrack<R> extends AbstractJavaImpl<R> {

	public List<R> Method(SD<R> body) {
		List<R> result = new ArrayList<>();
		try {
			body.accept(ret -> {
				result.add(ret);
			}, () -> {}, l -> {}, () -> {}, exc -> { throw new RuntimeException(exc); });
		}
		catch (Fail f) {
			
		}
		return result;
	}
	
	private static final class Fail extends RuntimeException {
		
	}
	
	public <T> SD<R> Choose(ED<Iterable<T>> choices, Function<? super T, SD<R>> body) {
		return (rho, sigma, contin, brk, err) -> {
			choices.accept(iter -> {
				for (T t: iter) {
					try {
						System.out.println("Trying " + t);
						body.apply(t).accept(rho, sigma, contin, brk, err);
						//break; // ????
					}
					catch (Fail f) {
						System.out.println("Fail for " + t);
					}
				}
				throw new Fail();
			}, err);;
		};
	}
	
	public SD<R> Guard(ED<Boolean> cond) {
		return (rho, sigma, contin, brk, err) -> {
			cond.accept(b -> {
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
