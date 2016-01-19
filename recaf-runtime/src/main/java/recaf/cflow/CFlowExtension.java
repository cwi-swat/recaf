package recaf.cflow;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.Ref;
import recaf.core.SD;

public class CFlowExtension<R> extends AbstractJavaCPS<R> {
	public R Method(SD<R> body) {
		Ref<R> result = new Ref<>();
		body.accept(r -> { result.x = r; }, () -> {}, exc -> { throw new RuntimeException(exc); });
		return result.x;
	}
	
	public SD<R> Until(ED<Boolean> cond, SD<R> body) {
		return Seq2(body, While((k, err) -> cond.accept(b -> k.accept(!b), err), body));
	}
	
	public SD<R> Loop(SD<R> body) {
		return While(Exp(() -> true), body);
	}

	public SD<R> Unless(ED<Boolean> cond, SD<R> body) {
		return While((k, err) -> cond.accept(b -> k.accept(!b), err), body);
	}
	
}
