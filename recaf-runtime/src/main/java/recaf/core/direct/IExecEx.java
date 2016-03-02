package recaf.core.direct;

import recaf.core.direct.EvalJavaStmt.Jump;

public interface IExecEx <E extends Throwable> extends IExec {
	void execWithEx(String label) throws Throwable;
	
    @Override
    default void exec(String label) throws Exception {
    	try {
    		execWithEx(label);
		} catch (final Throwable exception) {
			throw new SuspendedEx(exception);
		}
    }
}

