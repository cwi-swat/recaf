package recaf.cflow;

import recaf.core.ED;
import recaf.core.SD;

public class Unless<R> extends CFlow<R> {
	public SD<R> Unless(ED<Boolean> cond, SD<R> body) {
		return If((k, err) -> cond.accept(b -> k.accept(!b), err), body);
	}
}
