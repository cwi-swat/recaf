package recaf.dummy;

import recaf.core.AbstractJavaImpl;
import recaf.core.Cont;
import recaf.core.Ref;

public class DummyExtension<R> extends AbstractJavaImpl<R> {

	public R Method(Cont<R> body) {
		Ref<R> ret = new Ref<>();
		body.statementDenotation.accept(r -> {
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
