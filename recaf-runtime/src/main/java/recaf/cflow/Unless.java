package recaf.cflow;

import recaf.core.ED;
import recaf.core.SD;

public class Unless<R> extends CFlow<R> {
	public Cont<R> Unless(Cont<Boolean> cond, Cont<R> body) {
		return If(Cont.fromED((k, err) -> cond.expressionDenotation.accept(b -> k.accept(!b), err)), body);
	}
}
