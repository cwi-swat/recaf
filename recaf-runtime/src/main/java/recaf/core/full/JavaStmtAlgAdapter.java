package recaf.core.full;

import static recaf.core.expr.EvalJavaHelper.toValue;

import java.util.function.Function;

import recaf.core.Ref;
import recaf.core.alg.JavaStmtAlg;
import recaf.core.alg.JavaStmtOnlyAlg;
import recaf.core.direct.IEval;

public interface JavaStmtAlgAdapter<R, S, C> extends JavaStmtAlg<R, IEval, S, C>{
	JavaStmtOnlyAlg<R, S, C> base();

	@Override
	default <T> S Decl(IEval exp, Function<Ref<T>, S> body) {
		return base().Decl(() -> (T)toValue(exp.eval()), body);
	}

	@Override
	default <T> S ForEach(IEval exp, Function<Ref<T>, S> body) {
		return base().ForEach(() -> (Iterable<T>)toValue(exp.eval()), body); 
	}

	@Override
	default <T> S ForDecl(IEval init, Function<Ref<T>, S> body) {
		return base().ForDecl(() -> (T)toValue(init.eval()), body); 
	}

	@Override
	default S ForBody(IEval cond, S update, S body) {
		return base().ForBody(() -> (Boolean)toValue(cond.eval()), update, body);
	}

	@Override
	default <T extends Throwable> S Throw(IEval e) {
		return base().Throw(() -> (Throwable)toValue(e.eval()));
	}

	@Override
	default <T extends Throwable> S TryCatch(S body, Class<T> type, Function<T, S> handle) {
		return base().TryCatch(body, type, handle);
	}

	@Override
	default <T> S Switch(IEval expr, C... cases) {
		return base().Switch(() -> toValue(expr.eval()), cases);
	}

	@Override
	default <T> C Case(T constant, S expStat) {
		return base().Case(constant, expStat);
	}

	@Override
	default C Default(S expStat) {
		return base().Default(expStat);
	}

	@Override
	default S For(S init, IEval cond, S update, S body) {
		return base().For(init, () -> (Boolean)toValue(cond.eval()), update, body);
	}

	@Override
	default S If(IEval cond, S s) {
		return base().If(() -> (Boolean)toValue(cond.eval()), s);
	}

	@Override
	default S If(IEval cond, S s1, S s2) {
		return base().If(() -> (Boolean)toValue(cond.eval()), s1, s2);
	}

	@Override
	default S While(IEval cond, S s) {
		return base().While(() -> (Boolean)toValue(cond.eval()), s);
	}

	@Override
	default S DoWhile(S s, IEval cond) {
		return base().DoWhile(s, () -> (Boolean)toValue(cond.eval()));
	}

	@Override
	default S Labeled(String label, S s) {
		return base().Labeled(label, s);
	}

	@Override
	default S Break(String label) {
		return base().Break(label);
	}

	@Override
	default S Continue(String label) {
		return base().Continue(label);
	}

	@Override
	default S Break() {
		return base().Break();
	}

	@Override
	default S Continue() {
		return base().Break();
	}

	@Override
	default S Return(IEval supplier) {
		return base().Return(() -> (R)toValue(supplier.eval()));
	}

	@Override
	default S Return() {
		return base().Return();
	}

	@Override
	default S Empty() {
		return base().Empty();
	}

	@Override
	default S Seq(S s1, S s2) {
		return base().Seq(s1, s2);
	}

	@Override
	default S TryFinally(S body, S fin) {
		return base().TryFinally(body, fin);
	}

	@Override
	default S ExpStat(IEval e) {
		return base().ExpStat(() -> { e.eval(); return null; });
	}
	
	
}
