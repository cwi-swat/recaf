package recaf.core.direct;

import static recaf.core.EvalJavaHelper.toValue;

import recaf.core.alg.JavaMethodAlg;


public interface NoOp<R> extends FullJava<R>, JavaMethodAlg<R, IExecEx<?>> {

	@SuppressWarnings("unchecked")
	@Override
	default R Method(IExecEx<?> body) {
		try {
			body.exec(null);
		}
		catch (Return r) {
			return (R) toValue(r.getValue());
		} 
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
}
