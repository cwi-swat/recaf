package recaf.visit;

import java.util.List;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class VisitExtension<R> extends AbstractJavaCPS<R> {

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
