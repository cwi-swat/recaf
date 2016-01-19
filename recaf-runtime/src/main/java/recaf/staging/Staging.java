package recaf.staging;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.SD;

public class Staging<R> extends AbstractJavaCPS<R> {
	public SD<R> If(Boolean b, SD<R> s1) {
		return If(b, s1, Empty());
	}

	public SD<R> If(Boolean b, SD<R> s1, SD<R> s2) {
		return b ? s1 : s2;
	}
	
	@SuppressWarnings("unchecked")
	public <U> SD<R> For(Iterable<U> coll, Function<U, SD<R>> body) {
		List<SD<R>> lst = new ArrayList<>();
		for (U u: coll) {
			lst.add(body.apply(u));
		}
		return Seq((SD<R>[]) lst.toArray());
	}
	
	public <U> SD<R> Decl(U exp, Function<U, SD<R>> body) {
		return body.apply(exp);
	}
}
