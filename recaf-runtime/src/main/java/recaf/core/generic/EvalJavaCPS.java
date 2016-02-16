package recaf.core.generic;

import java.util.function.Function;
import java.util.function.Supplier;

import higher.App;
import recaf.core.cps.K;
import recaf.core.cps.K0;

public class EvalJavaCPS<R> implements AbstractJava<R, StmCPS.t, CaseCPS.t, ExpCPS.t> {

	@Override
	public App<StmCPS.t, R> Empty() {
		return new StmCPS<R>() {
			@Override
			public void accept(K<R> rho, K0 sigma, K<String> brk, K<String> contin, K<Throwable> err) {
				sigma.call();
			}
		};
	}
	
	@Override
	public <T> App<ExpCPS.t, T> Exp(Supplier<T> e) {
		return null;
	}

	@Override
	public App<StmCPS.t, R> If(App<ExpCPS.t, Boolean> e, App<StmCPS.t, R> s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> If(App<ExpCPS.t, Boolean> e, App<StmCPS.t, R> s1, App<StmCPS.t, R> s2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> Labeled(String label, App<StmCPS.t, R> s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> While(App<ExpCPS.t, Boolean> e, App<StmCPS.t, R> s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> DoWhile(App<StmCPS.t, R> s, App<ExpCPS.t, Boolean> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> Switch(App<ExpCPS.t, Integer> expr,
			App<CaseCPS.t, R>... cases) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<CaseCPS.t, R> Case(App<ExpCPS.t, Integer> constant,
			App<StmCPS.t, R> expStat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<CaseCPS.t, R> Default(App<StmCPS.t, R> expStat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> Break() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> Break(String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> Continue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> Continue(String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> Return(App<ExpCPS.t, R> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> Return() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> Seq(App<StmCPS.t, R>... ss) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Throwable> App<StmCPS.t, R> Throw(App<ExpCPS.t, T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Throwable> App<StmCPS.t, R> TryCatch(App<StmCPS.t, R> body, Class<T> type, Function<T, App<StmCPS.t, R>> handle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public App<StmCPS.t, R> TryFinally(App<StmCPS.t, R> body, App<StmCPS.t, R> fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U> App<StmCPS.t, R> ExpStat(K0 thunk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U> App<StmCPS.t, R> Decl(App<ExpCPS.t, U> exp, Function<U, App<StmCPS.t, R>> body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U> App<StmCPS.t, R> For(App<ExpCPS.t, Iterable<U>> coll, Function<U, App<StmCPS.t, R>> body) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
