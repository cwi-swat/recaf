package recaf.cflow;

import recaf.core.cps.ED;
import recaf.core.cps.SD;

public class Until<R> extends CFlow<R> {

	public SD<R> Until(ED<Boolean> cond, SD<R> body) {
		return Seq(body, While((k, err) -> cond.accept(b -> k.accept(!b), err), body));
	}

}
