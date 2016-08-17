package recaf.core.cps.effects;

import java.util.function.Function;

public interface EffT<R, _Eff extends Effect> {
	_Eff accept(Function<R, _Eff> rho, Function<Throwable, _Eff> err, EffCont<_Eff> effectCont);
}