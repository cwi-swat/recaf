package recaf.paper.demo;

import java.util.function.Function;

import recaf.paper.expr.IEval;
import recaf.paper.full.FullMuJavaFromGenericCPS;
import recaf.paper.stm.SD;

public class BacktrackFull<R> implements FullMuJavaFromGenericCPS<R> {

	@Override
	public Backtrack<R> base() {
		return new Backtrack<R>() {
		};
	}

	// Should be in Choose interface (?)
	public <T> SD<R> Choose(IEval e, Function<T, SD<R>> body) {
		return base().Choose(() -> (Iterable<T>) adapt(e), body);
	}

}