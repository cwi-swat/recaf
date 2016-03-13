package recaf.paper.access;

import recaf.paper.expr.IEval;
import recaf.paper.expr.MuExpJavaBase;
import recaf.paper.full.MuStmJavaAdapter;
import recaf.paper.methods.TPDirect;
import recaf.paper.stm.IExec;
import recaf.paper.stm.MuJava;
import recaf.paper.stm.MuJavaBase;

public class Security<R> implements TPDirect<R>, MuStmJavaAdapter<R, IExec>, MuExpJavaBase {

	@Override
	public MuJava<R, IExec> base() {
		return new MuJavaBase<R>() {};
	}
	
	public Security(Policy policy) {
		this.policy = policy;
	}
	

	//BEGIN_SECURITY
	private Policy policy;
	
	@Override
	public IEval Field(IEval x, String f) {
		return () -> {
			Object obj = x.eval();
			if (policy.isAllowed(obj, f)){
				return MuExpJavaBase.super
				  .Field(() -> obj, f).eval();
			}
			return null;
		};
	}
	//END_SECURITY
}
