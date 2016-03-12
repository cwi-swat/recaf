package recaf.paper.stm;

import java.util.function.Function;
import java.util.function.Supplier;

//BEGIN_MUJAVA_IMPL
public interface MuJavaBase<R> extends MuJava<R, IExec> {
	static <T> T run(IExec s) {
		try { s.exec(); } 
		catch (Return r) { return (T)r.value; }
		catch (Throwable e) { throw new RuntimeException(e); }
	    return null;
	}
	
	default IExec Exp(Supplier<Void> e) {
		return () -> { e.get(); };
	}

	default <T> IExec Decl(Supplier<T> e, Function<T, IExec> s) {
		return () -> s.apply(e.get()).exec();
	}

	default <T> IExec For(Supplier<Iterable<T>> e, Function<T, IExec> s) {
		return () -> { for (T t: e.get()) s.apply(t).exec(); };
	}

	default IExec If(Supplier<Boolean> c, IExec s1, IExec s2) {
		return () -> { if (c.get()) s1.exec(); else  s2.exec(); };
	}

	default IExec Return(Supplier<R> e) {
		return () -> { throw new Return(e.get()); };
	}

	default IExec Seq(IExec s1, IExec s2) {
		return () -> { s1.exec(); s2.exec(); };
	}

	default IExec Empty() {
		return () -> {};
	}
}
//END_MUJAVA_IMPL
