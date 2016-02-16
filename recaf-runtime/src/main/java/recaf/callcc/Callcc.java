package recaf.callcc;

import java.util.function.Consumer;
import java.util.function.Function;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.functional.K0;
import recaf.core.functional.SD;

public class Callcc<R> extends AbstractJavaImpl<R> {

	public R Method(SD<R> body) {
		return typePreserving(body);
	}
	
	/*
	 * From wikipedia:
	 * Taking a function f as its only argument, call/cc takes the 
	 * current continuation (i.e., a "snapshot" of the current control 
	 * context or control state of the program) as an object and applies 
	 * f to it. The continuation object is a first-class value and is 
	 * represented as a function, with function application as its only operation.
	 */
	public SD<R> Callcc(Function<K0, SD<R>> body) {
		return (rho, sigma, contin, brk, err) -> {
			body.apply(sigma).accept(rho, sigma, contin, brk, err);
		};
	}
}
