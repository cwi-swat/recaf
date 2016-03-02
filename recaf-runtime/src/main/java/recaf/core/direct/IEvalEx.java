package recaf.core.direct;

public interface IEvalEx<E extends Throwable> extends IEval {
	Object evalWithEx() throws E;
	
    @Override
    default Object eval() {
    	try {
			return evalWithEx();
		} catch (final Throwable exception) {
			throw new SuspendedEx(exception);
		}
    }
}
