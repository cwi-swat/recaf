package recaf.delim;

import recaf.core.cps.AbstractJavaImpl;
import recaf.core.cps.SD;

public class ResetExtension<R> extends AbstractJavaImpl<R> {

	public Shift<R, R, R> Method(SD<R> body) {
		body.accept(null, v -> {}, ()-> {}, l -> {}, l -> {}, ex -> {});
		return null;
	}

//	public <T> Cont<R> Await(Cont<CompletableFuture<T>> e, Function<T, Cont<R>> body) {
//		throw new UnsupportedOperationException();
//	}
}
