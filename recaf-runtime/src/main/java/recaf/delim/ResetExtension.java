package recaf.delim;

import recaf.core.AbstractJavaImpl;

public class ResetExtension<R> extends AbstractJavaImpl<R> {

	public Shift<R, R, R> Method(Cont<R> body) {
		
		body.statementDenotation.accept(v -> {}, ()-> {}, ex -> {});
		return null;
	}

//	public <T> Cont<R> Await(Cont<CompletableFuture<T>> e, Function<T, Cont<R>> body) {
//		throw new UnsupportedOperationException();
//	}
}
