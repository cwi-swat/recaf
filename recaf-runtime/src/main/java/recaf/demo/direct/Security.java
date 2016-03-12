package recaf.demo.direct;

import static recaf.core.expr.EvalJavaHelper.toValue;

import recaf.core.direct.IEval;
import recaf.core.expr.ReflectRef;
import recaf.core.full.FullJavaDirect;

public class Security<R> implements FullJavaDirect<R> {
	
	private Policy policy;
	
	public Security(Policy policy) {
		this.policy = policy;
	}
	
	@Override
	public IEval Field(IEval recv, String name) {
		return () -> {
			Object obj = recv.eval();
			if (policy.check(Policy.READ, toValue(obj), name)){
				return FullJavaDirect.super.Field(() -> obj, name).eval();
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
				if (policy.check(Policy.UPDATE, toValue(recv), fldName)){
					return FullJavaDirect.super.Assign(() -> obj, rhs).eval();
				}
				else
					return null;
			}
			else
				return FullJavaDirect.super.Assign(() -> obj, rhs).eval();
		};
	}

	

}