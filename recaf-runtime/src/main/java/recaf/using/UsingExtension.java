package recaf.using;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.Ref;
import recaf.core.SD;

public class UsingExtension<R> extends AbstractJavaCPS<R> {
	
	public R Method(SD<R> body) {
		Ref<R> result = new Ref<>();
		body.accept(r -> { result.x = r; }, () -> {}, exc -> { throw new RuntimeException(exc); });
		return result.x;
	}
	
	public SD<R> Using(ED<Closeable> resource, Function<Closeable, SD<R>> body) {
		return (rho, sigma, err) -> {
			resource.accept(t -> {
				body.apply(t).accept(r -> {
					try {
						t.close();
					} catch (IOException e) {
						err.accept(e);
					}
					rho.accept(r);
				}, () -> {
					try {
						t.close();
					} catch (IOException e) {
						err.accept(e);
					}
					sigma.call();
				}, exc -> {
					try {
						t.close();
					} catch (IOException e) {
						err.accept(e);
					}
					err.accept(exc);
				});
			}, err);
		};
	}
}
