package recaf.paper.demo;

import java.util.function.Function;

import recaf.paper.expr.IEval;
import recaf.paper.full.FullMuJavaCPS;
import recaf.paper.stm.SD;

public 
//BEGIN_BACKTRACK_FULL
interface BacktrackFull<R> extends FullMuJavaCPS<R> {
	default Backtrack<R> base() {
		return new Backtrack<R>() {};
	}
	default <T> SD<R> Choose(IEval e, Function<T, SD<R>> body){
		return base().Choose(adapt(e), body);
	}
}
//END_BACKTRACK_FULL