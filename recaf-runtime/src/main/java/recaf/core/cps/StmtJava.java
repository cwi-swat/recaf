package recaf.core.cps;

import recaf.core.ISupply;

//public interface StmtJava<R> extends JavaStmtAlg<R, ISupply<?>, SD<R>, CD<R>> {
public interface StmtJava<R> extends EvalJavaStmt<R, ISupply<?>>  {
	
//	default SD<R> ExpStat(ISupply<?> thunk) {
//		return (label, rho, sigma, brk, contin, err) -> {
//			try {
//				thunk.get();
//			} catch (Throwable t) {
//				err.accept(t);
//				return;
//			}
//			sigma.call();
//		};
//	}
}
