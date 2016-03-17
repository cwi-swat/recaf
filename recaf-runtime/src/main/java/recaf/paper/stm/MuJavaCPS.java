package recaf.paper.stm;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.cps.K0;

// TODO: need to distingui R as return type and R provided to SD
// otherwise yield etc (and backtracking don't work).

//BEGIN_MUJAVA_CPS
public interface MuJavaCPS<R> extends MuJava<R, SD<R>> {
	default SD<R> Exp(Supplier<Void> e) {
		return (r, s) -> { e.get(); s.call(); };
	}

	default <T> SD<R> Decl(Supplier<T> e, Function<T, SD<R>> s) {
		return (r, s0) -> s.apply(e.get()).accept(r, s0);
	}

	default <T> SD<R> For(Supplier<Iterable<T>> e, Function<T, SD<R>> s) {
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

	default SD<R> If(Supplier<Boolean> c, SD<R> s1, SD<R> s2) {
		return (r, s) -> {
			if (c.get()) s1.accept(r, s); 
			else s2.accept(r, s);};
	}

	default SD<R> Return(Supplier<R> e) {
		return (r, s) -> r.accept(e.get());
	}

	default SD<R> Seq(SD<R> s1, SD<R> s2) {
		return (r, s) -> s1.accept(r, () -> s2.accept(r, s));
	}

	default SD<R> Empty() {
		return (r, s) -> s.call();
	}
}
//END_MUJAVA_CPS
