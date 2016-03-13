package recaf.paper.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.paper.methods.MuJavaMethod;
import recaf.paper.stm.MuJavaCPS;
import recaf.paper.stm.SD;

//BEGIN_CHOOSE
interface Choose<R> extends MuJavaCPS<R>, MuJavaMethod<List<R>, SD<R>> {
	@Override
	default List<R> Method(SD<R> body) {
		List<R> result = new ArrayList<>();
		body.accept(ret -> { result.add(ret); }, () -> {});
		return result;
	}

	default <T> SD<R> Choose(Supplier<Iterable<T>> e, Function<T, SD<R>> s) {
		return (r, s0) -> {
			for (T t: e.get()) 
				s.apply(t).accept(r, s0);
		}; 
	}
}
//END_CHOOSE
