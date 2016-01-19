package builders.using;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import builders.core.AbstractJavaCPS;
import builders.core.ED;
import builders.core.SD;

public class UsingExtension<R> extends AbstractJavaCPS<R> {
	
	@SuppressWarnings("unchecked")
	public R Method(SD<R> body) {
		R result[] = (R[]) new Object[] {null};
		body.accept(r -> { result[0] = r; }, () -> {}, exc -> { throw new RuntimeException(exc); });
		return result[0];
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
