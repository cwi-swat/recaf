package recaf.paper.demo;

import java.util.function.Supplier;

import recaf.paper.stm.MuJava;

public
//BEGIN_UNLESS
interface Unless<R, S> extends MuJava<R, S> {
	default S Unless(Supplier<Boolean> c, S s) {
		return If(() -> !c.get(), s, Empty());
	}
}
//END_UNLESS
