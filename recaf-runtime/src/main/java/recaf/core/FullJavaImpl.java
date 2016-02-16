package recaf.core;

import recaf.core.functional.SD;

public class FullJavaImpl<R> extends AbstractJavaLessTypes<R> implements JavaExprEvaluator{  
/*
	public R Method(SD<R> body) {
		Ref<R> ret = new Ref<>();
		body.accept(r -> {
			ret.value = r;
		} , () -> {
		} , (s) -> {
		} , () -> {
		} , exc -> {
			throw new RuntimeException(exc);
		});
		return ret.value;
	}
*/
	public R Method(SD<R> body) {
		return typePreserving(body);
	}
}