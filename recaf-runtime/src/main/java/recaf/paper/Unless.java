package recaf.paper;

import java.util.function.Supplier;

public interface Unless<R, S> extends MuJava<R, S> {
	default 
	//BEGIN_UNLESS
	S Unless(Supplier<Boolean> c, S s) {
		return If(() -> !c.get(), s, Empty());
	}
	//END_UNLESS
}
