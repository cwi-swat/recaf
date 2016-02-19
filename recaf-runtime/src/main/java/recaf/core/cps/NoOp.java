package recaf.core.cps;

import recaf.core.Ref;
import recaf.core.alg.JavaMethodAlg;

// alterantive names: DefaultMethod, TypePreserving
public interface NoOp<R> extends StmtJava<R>, JavaMethodAlg<R, SD<R>> {
	@Override
	default R Method(SD<R> body) {
		Ref<R> result = new Ref<>();
		body.accept(null, r -> {
			result.value = r;
		} , () -> {
		} , l -> {
			throw new AssertionError("Cannot call break without loop");
		} , l -> {
			throw new AssertionError("Cannot call continue without loop");
		} , exc -> {
			throw new RuntimeException(exc);
		});
		return result.value;
	}
}