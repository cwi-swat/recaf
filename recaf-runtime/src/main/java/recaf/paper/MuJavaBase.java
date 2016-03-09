package recaf.paper;

import java.util.function.Function;
import java.util.function.Supplier;

//BEGIN_MUJAVA_IMPL
interface IExec { void exec(); }

class Return extends RuntimeException { 
	Object value;
	Return(Object value) { this.value = value; }
}

public class MuJavaBase<R> implements MuJava<R, IExec> {
	public R Method(IExec s) {
		try { s.exec(); } catch (Return r) { return (R)r.value; }
		return null;
	}

	public IExec Exp(Supplier<Void> e) {
		return () -> { e.get(); };
	}

	public <T> IExec Decl(Supplier<T> e, Function<T, IExec> s) {
		return () -> s.apply(e.get()).exec();
	}

	public <T> IExec For(Supplier<Iterable<T>> e, Function<T, IExec> s) {
		return () -> { for (T t: e.get()) s.apply(t).exec(); };
	}

	public IExec If(Supplier<Boolean> c, IExec s1, IExec s2) {
		return () -> { if (c.get()) s1.exec(); else  s2.exec(); };
	}

	public IExec Return(Supplier<? extends R> e) {
		return () -> { throw new Return(e.get()); };
	}

	public IExec Seq(IExec s1, IExec s2) {
		return () -> { s1.exec(); s2.exec(); };
	}

	public IExec Empty() {
		return () -> {};
	}
}
//END_MUJAVA_IMPL
