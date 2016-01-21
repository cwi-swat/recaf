package recaf.cflow;

import recaf.core.ED;
import recaf.core.SD;

public class Until<R> extends CFlow<R> {

	public SD<R> Until(ED<Boolean> cond, SD<R> body) {
		return Seq2(body, While((k, err) -> cond.accept(b -> k.accept(!b), err), body));
	}
	
}
