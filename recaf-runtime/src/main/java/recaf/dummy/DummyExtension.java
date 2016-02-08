package recaf.dummy;

import recaf.core.AbstractJavaImpl;
import recaf.core.Ref;
import recaf.core.functional.SD;

public class DummyExtension<R> extends AbstractJavaImpl<R> {

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
}
