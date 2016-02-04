package recaf.cflow;

import recaf.core.Cont;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class Until<R> extends CFlow<R> {

	public Cont<R> Until(Cont<Boolean> cond, Cont<R> body) {
		return Seq2(body, While(Cont.fromED((k, err) -> cond.expressionDenotation.accept(b -> k.accept(!b), err)), body));
	}
	
}
