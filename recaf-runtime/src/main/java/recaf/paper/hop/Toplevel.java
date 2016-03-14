package recaf.paper.hop;

import java.util.function.Function;

import recaf.paper.demo.ToJS;
import recaf.paper.expr.IEval;
import recaf.paper.expr.MuExpJava;
import recaf.paper.full.MuStmJava;
import recaf.paper.stm.IExec;

public class Toplevel<S, E> implements MuStmJava<Function<MuStmJava<IExec, IEval>, IExec>, Function<MuExpJava<E>, E>> {

	@Override
	public Function<MuStmJava<IExec, IEval>, IExec> Exp(Function<MuExpJava<E>, E> x) {
		return alg -> { x.apply(alg).eval(); }
	}

	@Override
	public <T> Function<MuStmJava<IExec, IEval>, IExec> Decl(Function<MuExpJava<E>, E> x,
			Function<T, Function<MuStmJava<IExec, IEval>, IExec>> s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Function<MuStmJava<IExec, IEval>, IExec> For(Function<MuExpJava<E>, E> x,
			Function<T, Function<MuStmJava<IExec, IEval>, IExec>> s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Function<MuStmJava<IExec, IEval>, IExec> If(Function<MuExpJava<E>, E> c, Function<MuStmJava<IExec, IEval>, IExec> s1,
			Function<MuStmJava<IExec, IEval>, IExec> s2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Function<MuStmJava<IExec, IEval>, IExec> Return(Function<MuExpJava<E>, E> x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Function<MuStmJava<IExec, IEval>, IExec> Seq(Function<MuStmJava<IExec, IEval>, IExec> s1, Function<MuStmJava<IExec, IEval>, IExec> s2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Function<MuStmJava<IExec, IEval>, IExec> Empty() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Function<MuStmJava<IExec, IEval>, IExec> Client(Function<MuStmJava<String, String>, IExec> body) {
		return alg -> {
			return () -> { body.apply(new ToJS()); };
		};
	}

}
