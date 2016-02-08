package recaf.visit;

import java.util.List;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class VisitExtension<R> extends AbstractJavaImpl<R> {

	public R Method(SD<R> body) {
		return null;
	}
	
	public <T> SD<R> Visit(ED<T> exp, List<SD<R>> cases) {
		return null;
	}
	
	public <T> SD<R> When(Function<T, SD<R>> body) {
		return null;
	}
	
}
