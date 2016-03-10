package recaf.demo.direct;

import static recaf.core.EvalJavaHelper.toValue;

import recaf.core.ReflectRef;
import recaf.core.direct.IEval;
import recaf.core.direct.NoOp;

public class Security<R> implements NoOp<R> {
	
	private Policy policy;
	
	public Security(Policy policy) {
		this.policy = policy;
	}
	
	@Override
	public IEval Field(IEval recv, String name) {
		return () -> {
			Object obj = recv.eval();
			if (policy.check(SecurityOperation.READ, toValue(obj), name)){
				return NoOp.super.Field(() -> obj, name).eval();
			}
			else
				return null;
		};
	}
	
	@Override
	public IEval Assign(IEval lhs, IEval rhs) {
		return () -> {
			Object obj = lhs.eval();
			if (obj instanceof ReflectRef){
				String fldName = ((ReflectRef<?>) obj).getFieldName();
				Object recv = ((ReflectRef<?>) obj).getObject();
				if (policy.check(SecurityOperation.UPDATE, toValue(recv), fldName)){
					return NoOp.super.Assign(() -> obj, rhs).eval();
				}
				else
					return null;
			}
			else
				return NoOp.super.Assign(() -> obj, rhs);
		};
	}

	

}