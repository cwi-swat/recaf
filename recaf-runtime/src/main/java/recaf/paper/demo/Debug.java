package recaf.paper.demo;

import recaf.paper.expr.IEval;
import recaf.paper.expr.Pair;
import recaf.paper.expr.PrintAndEval;
import recaf.paper.full.MuStmPrintEvalAdapter;
import recaf.paper.methods.TPDirect;
import recaf.paper.stm.IExec;
import recaf.paper.stm.MuJava;
import recaf.paper.stm.MuJavaBase;

public class Debug<R> implements MuStmPrintEvalAdapter<R, IExec>, PrintAndEval, TPDirect<R> {

	@Override
	public MuJava<R, IExec> base() {
		return new MuJavaBase<R>() {
		};
	}

	public 
	//BEGIN_DEBUG
	IExec Debug(Pair<IEval, String> e) {
		return () -> System.err.println("DEBUG: " + e.snd 
				          + " => " + e.fst.eval());
	}
	//END_DEBUG
}