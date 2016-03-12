package recaf.paper.methods;

import recaf.paper.stm.SD;

public interface TPCPS<R> {
	default R Method(SD<R> s) {
		R r[] = (R[]) new Object[1];
		s.accept(v -> r[0] = v, () -> {});
		return r[0];
	}
}
