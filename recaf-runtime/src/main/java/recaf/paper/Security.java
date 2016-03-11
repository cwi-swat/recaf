package recaf.paper;

import static recaf.core.EvalJavaHelper.toValue;

import recaf.core.direct.IEval;
import recaf.core.direct.NoOp;

//BEGIN_SECURITY
public class Security<R> implements NoOp<R> {
	private Policy policy;
	public Security(Policy policy) {
		this.policy = policy;
	}
	@Override
	public IEval Field(IEval recv, String name) {
		return () -> {
			Object obj = recv.eval();
			if (policy.checkRead(toValue(obj), name)){
				return NoOp.super.Field(() -> obj, name).eval();
			}
			else
				return null;
		};
	}
}
//END_SECURITY