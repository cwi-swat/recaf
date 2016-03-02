package recaf.core.direct;

import recaf.core.direct.EvalJavaStmt.Jump;
import recaf.core.direct.EvalJavaStmt.Return;

public interface IExecEx <E extends Throwable> extends IExec {
	void execWithEx(String label) throws Throwable;
	
    @Override
    default void exec(String label) throws Return, Jump {
    	try {
    		execWithEx(label);
		} catch (final Throwable exception) {
			throw new SuspendedEx(exception);
		}
    }
}

