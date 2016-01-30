package recaf.visit;

import java.util.List;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.ED;
import recaf.core.SD;

public class VisitExtension<R> extends AbstractJavaImpl<R> {

	public R Method(Cont<R> body) {
		return null;
	}
	
	public <T> Cont<R> Visit(Cont<T> exp, List<Cont<R>> cases) {
		return null;
	}
	
	public <T> Cont<R> When(Function<T, Cont<R>> body) {
		return null;
	}
	
}
