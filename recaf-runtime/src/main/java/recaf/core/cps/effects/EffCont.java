package recaf.core.cps.effects;

import java.util.function.BiFunction;

public interface EffCont<_Eff extends Effect> {
	_Eff accept(_Eff eff, BiFunction<_Eff, EffCont<_Eff>, _Eff> f);
}
