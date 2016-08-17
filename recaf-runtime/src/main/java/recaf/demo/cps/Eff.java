package recaf.demo.cps;

import java.util.List;
import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.Ref;
import recaf.core.alg.JavaMethodAlg;
import recaf.core.alg.JavaStmtOnlyAlg;
import recaf.core.cps.CD;
import recaf.core.cps.effects.EffT;
import recaf.core.cps.effects.Effect;

public class Eff<R> implements JavaStmtOnlyAlg<R, EffT<R, ? extends Effect>, CD<R>>, JavaMethodAlg<Effect, Effect> {

	public <T> Effect Choose(ISupply<Iterable<T>> choices, Function<? super T, ? extends Effect> body) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Effect Method(Effect body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> EffT<R, ?> Decl(ISupply<T> exp, Function<Ref<T>, EffT<R, ?>> body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> EffT<R, ?> ForEach(ISupply<Iterable<T>> exp, Function<Ref<T>, EffT<R, ?>> body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> EffT<R, ?> ForDecl(ISupply<T> init, Function<Ref<T>, EffT<R, ?>> body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> ForBody(ISupply<Boolean> cond, EffT<R, ?> update, EffT<R, ?> body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Throwable> EffT<R, ?> Throw(ISupply<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Throwable> EffT<R, ?> TryCatch(EffT<R, ?> body, Class<T> type, Function<T, EffT<R, ?>> handle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> EffT<R, ?> Switch(ISupply<T> expr, CD<R>... cases) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> CD<R> Case(T constant, EffT<R, ?> expStat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CD<R> Default(EffT<R, ?> expStat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> For(EffT<R, ?> init, ISupply<Boolean> cond, EffT<R, ?> update, EffT<R, ?> body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> If(ISupply<Boolean> cond, EffT<R, ?> s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> If(ISupply<Boolean> cond, EffT<R, ?> s1, EffT<R, ?> s2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> While(ISupply<Boolean> cond, EffT<R, ?> s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> DoWhile(EffT<R, ?> s, ISupply<Boolean> cond) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> Labeled(String label, EffT<R, ?> s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> Break(String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> Continue(String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> Break() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> Continue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> Return(ISupply<R> supplier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> Return() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> Empty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> Seq(EffT<R, ?> s1, EffT<R, ?> s2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> TryFinally(EffT<R, ?> body, EffT<R, ?> fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EffT<R, ?> ExpStat(ISupply<Void> e) {
		// TODO Auto-generated method stub
		return null;
	}
}
