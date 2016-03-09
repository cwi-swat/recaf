package recaf.paper;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.cps.K;
import recaf.core.cps.K0;


//BEGIN_MUJAVA_CPS
interface SD<R> { void accept(K<R> r, K0 s); }

public class MuJavaCPS<R> implements MuJava<R, SD<R>> {
	public R Method(SD<R> s) {
		R r[] = (R[]) new Object[1];
		s.accept(v -> r[0] = v, () -> {});
		return r[0];
	}

	public SD<R> Exp(Supplier<Void> e) {
		return (r, s) -> { e.get(); s.call(); };
	}

	public <T> SD<R> Decl(Supplier<T> e, Function<T, SD<R>> s) {
		return (r, s0) -> s.apply(e.get()).accept(r, s0);
	}

	public <T> SD<R> For(Supplier<Iterable<T>> e, Function<T, SD<R>> s) {
		return (r, s0) -> {
			Iterator<T> iter = e.get().iterator();
			new K0() {
				public void call() {
					if (iter.hasNext()) 
						s.apply(iter.next()).accept(r, () -> call()); 
					else 
						s0.call();
				}
				
			}.call();
		};
	}

	public SD<R> If(Supplier<Boolean> c, SD<R> s1, SD<R> s2) {
		return (r, s) -> {if (c.get()) s1.accept(r, s); else s2.accept(r, s);};
	}

	public SD<R> Return(Supplier<? extends R> e) {
		return (r, s) -> r.accept(e.get());
	}

	public SD<R> Seq(SD<R> s1, SD<R> s2) {
		return (r, s) -> s1.accept(r, () -> s2.accept(r, s));
	}

	public SD<R> Empty() {
		return (r, s) -> s.call();
	}
}
//END_MUJAVA_CPS
