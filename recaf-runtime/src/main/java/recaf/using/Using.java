package recaf.using;

import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class Using<R> extends AbstractJavaImpl<R> {

	public R Method(SD<R> body) {
		return typePreserving(body);
	}
	
	public <U extends AutoCloseable> SD<R> Using(ED<U> resource, Function<U, SD<R>> body) {
		return (rho, sigma, brk, contin, err) -> {
			resource.accept(t -> {
				body.apply(t).accept(r -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
						return;
					}
					rho.accept(r);
				} , () -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
						return;
					}
					sigma.call();
				} , l -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
						return;
					}
					sigma.call();
				} , l -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
						return;
					}
					sigma.call();
				} , exc -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
						return;
					}
					err.accept(exc);
				});
			} , err);
		};
	}
}
