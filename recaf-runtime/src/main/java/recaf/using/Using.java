package recaf.using;

import java.io.IOException;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.Cont;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class Using<R> extends AbstractJavaImpl<R> {

	public R Method(Cont<R> body) {
		return typePreserving(body);
	}

	public <U extends AutoCloseable> Cont<R> Using(Cont<U> resource, Function<U, Cont<R>> body) {
		return Cont.fromSD((rho, sigma, brk, contin, err) -> {
			resource.expressionDenotation.accept(t -> {
				body.apply(t).statementDenotation.accept(r -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
					}
					rho.accept(r);
				} , () -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
					}
					sigma.call();
				} , () -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
					}
					sigma.call();
				} , () -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
					}
					sigma.call();
				} , exc -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
					}
					err.accept(exc);
				});
			} , err);
		});
	}
}
