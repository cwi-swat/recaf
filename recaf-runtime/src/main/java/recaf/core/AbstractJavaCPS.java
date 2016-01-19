package recaf.core;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AbstractJavaCPS<R> {
	public <T> ED<T> Exp(Supplier<T> e) {
		return (k, err) -> {
			T t = null;
			try {
				t = e.get();
			}
			catch (Throwable ex) {
				err.accept(ex);
				return;
			}
			k.accept(t);
		};
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Throwable> SD<R> TryCatch(SD<R> body, Class<T> type, Function<T, SD<R>> handle) {
		return (rho, sigma, err) -> {
			body.accept(rho, sigma, (Throwable exc) -> {
				if (type.isInstance(exc)) {
					handle.apply((T) exc).accept(rho, sigma, err);
				}
				else {
					err.accept(exc);
				}
			});
		};
	}
	
	public SD<R> TryFinally(SD<R> body, SD<R> fin) {
		return (rho, sigma, err) -> {
			body.accept(r -> {
				fin.accept(rho, () -> rho.accept(r), err);
			}, () -> {
				fin.accept(rho, sigma, err);
			}, (Throwable exc) -> {
				fin.accept(rho /* todo: exception here too */, () -> err.accept(exc), err);
			});
		};
	}
	
	public SD<R> Empty() {
		return (rho, sigma, err) -> sigma.call();
	}
	
	public SD<R> If(ED<Boolean> e, SD<R> s) {
		return If(e, s, Empty());
	}
	
	public SD<R> If(ED<Boolean> e, SD<R> s1, SD<R> s2) {
		return (rho, sigma, err) -> e.accept(x -> {
			if (x) {
				s1.accept(rho, sigma, err);
			}
			else {
				s2.accept(rho, sigma, err);
			}
		}, err); 
	}
		
	public SD<R> While(ED<Boolean> e, SD<R> s) {
		return (rho, sigma, err) -> {
			new K0() {
				public void call() {
					If(e, Seq2(s, (a, b, c) -> call())).accept(rho, sigma, err);;
				}
			}.call();
		};
	}

	protected SD<R> Seq2(SD<R> s1, SD<R> s2) {
		return (rho, sigma, err) -> s1.accept(rho, () -> s2.accept(rho, sigma, err), err);
	}
	
	public SD<R> Seq(List<SD<R>> ss) {
		assert ss.size() > 0;
		return ss.stream().reduce(this::Seq2).get();
	}
	
	public SD<R> Return(ED<R> e) {
		return (rho, sigma, err) -> e.accept(rho, err);
	}
	
	public <T extends Throwable> SD<R> Throw(ED<T> e) {
		return (rho, sigma, err) -> e.accept(r -> err.accept(r), err);
	}

	public <U> SD<R> ExpStat(ED<U> e) {
		return (rho, sigma, err) -> e.accept(ignored -> sigma.call(), err);
	}
	
	// HOAS
	// int x = 3; s ==> Let(Exp(3), x -> [[s]])
	// S Let(E exp, Function<E, S> body);
	public <U> SD<R> Decl(ED<U> exp, Function<U, SD<R>> body) {
		return (rho, sigma, err) -> exp.accept(r -> body.apply(r).accept(rho, sigma, err), err);
	}
	
	public <U> SD<R> For(ED<Iterable<U>> coll, Function<U, SD<R>> body) {
		return (rho, sigma, err) -> coll.accept(iterable -> {
			Iterator<U> iter = iterable.iterator();
			While((s, err2) -> s.accept(iter.hasNext()), 
					Decl((s, err2) -> s.accept(iter.next()), body))
			.accept(rho, sigma, err);
		}, err);
	}
	
}
