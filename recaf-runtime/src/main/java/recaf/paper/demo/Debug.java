package recaf.paper.demo;

import recaf.paper.expr.IEval;
import recaf.paper.expr.MuStmPrintEvalAdapter;
import recaf.paper.expr.Pair;
import recaf.paper.expr.PrintAndEvalReusing;
import recaf.paper.methods.TPDirect;
import recaf.paper.stm.IExec;
import recaf.paper.stm.MuJava;
import recaf.paper.stm.MuJavaBase;

public class Debug<R> implements MuStmPrintEvalAdapter<R, IExec>, PrintAndEvalReusing, TPDirect<R> {

	@Override
	public MuJava<R, IExec> base() {
		return new MuJavaBase<R>() {
		};
	}

	public <T> IExec Debug(Pair<IEval, String> e) {
		return () -> {
			T result = null;
			try {
				result = (T) e.fst.eval();
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
			System.out.println("DEBUG: "+e.snd + " => " + result);
		};
	}
}