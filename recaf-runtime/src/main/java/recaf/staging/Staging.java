package recaf.staging;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.SD;

public class Staging<R> extends AbstractJavaImpl<R> {
	public Cont<R> If(Boolean b, Cont<R> s1) {
		return If(b, s1, Empty());
	}

	public Cont<R> If(Boolean b, Cont<R> s1, Cont<R> s2) {
		return b ? s1 : s2;
	}
	
	@SuppressWarnings("unchecked")
	public <U> Cont<R> For(Iterable<U> coll, Function<U, Cont<R>> body) {
		List<Cont<R>> lst = new ArrayList<>();
		for (U u: coll) {
			lst.add(body.apply(u));
		}
		return Seq((Cont<R>[]) lst.toArray());
	}
	
	public <U> Cont<R> Decl(U exp, Function<U, Cont<R>> body) {
		return body.apply(exp);
	}
}
