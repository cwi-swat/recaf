package recaf.rollback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import recaf.core.AbstractJavaImpl;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class Transact<R> extends AbstractJavaImpl<R> {

	public R Method(SD<R> body) {
		return typePreserving(body);
	}
	
	private static class Transaction implements InvocationHandler {
		private final Object obj;
		private List<Action> actions;

		public Transaction(Object obj) {
			this.obj = obj;
			this.actions = new ArrayList<>();
		}
		
		private class Action {
			private Object[] args;
			private java.lang.reflect.Method method;

			public Action(java.lang.reflect.Method  m, Object[] args) {
				this.method = m;
				this.args = args;
			}
			
			public void execute() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
				method.invoke(obj, args);
			}
		}
		

		@Override
		public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
			actions.add(new Action(method, args));
			return null; // actions should be void methods.
		}

		public void commit() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			for (Action action: actions) {
				action.execute();
			}
		}
		
	}

	@SuppressWarnings("unchecked") // TODO: fix desugaring to add Class<T> everywhere.
	public <T extends ITransact> SD<R> Transact(Class<T> klass, ED<T> exp, Function<? super T, SD<R>> body) {
		assert klass.isInterface();
		return (rho, sigma, contin, brk, err) -> {
			exp.accept(v -> {
				Transaction trans = new Transaction(v);
				T proxy = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] {klass}, trans);
				body.apply(proxy).accept(rho, () -> {
					try {
						trans.commit();
					} catch (Exception e) {
						err.accept(e);
					}
				}, contin, brk, err);
			}, err);
		};
	}

	
	
}
