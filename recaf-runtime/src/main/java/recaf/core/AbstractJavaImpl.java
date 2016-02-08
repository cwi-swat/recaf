package recaf.core;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import recaf.core.functional.ED;
import recaf.core.functional.K0;
import recaf.core.functional.SD;

public class AbstractJavaImpl<R> { // implements AbstractJava<R> {

	protected R typePreserving(SD<R> body) {
		Ref<R> result = new Ref<>();
		body.accept(r -> {
			result.value = r;
		} , () -> {
		} , (s) -> {
		} , () -> {
		} , exc -> {
			throw new RuntimeException(exc);
		});
		return result.value;
	}

	public <T> ED<T> Exp(Supplier<T> e) {
		return (k, err) -> {
			T t = null;
			try {
				t = e.get();
			} catch (Throwable ex) {
				err.accept(ex);
				return;
			}
			k.accept(t);
		};
	}

	public SD<R> Empty() {
		return (rho, sigma, brk, contin, err) -> sigma.call();
	}

	public SD<R> If(ED<Boolean> e, SD<R> s) {
		return If(e, s, Empty());
	}

	public SD<R> If(ED<Boolean> e, SD<R> s1, SD<R> s2) {
		return (rho, sigma, brk, contin, err) -> e.accept(x -> {
			if (x) {
				s1.accept(rho, sigma, brk, contin, err);
			} else {
				s2.accept(rho, sigma, brk, contin, err);
			}
		} , err);
	}

	public SD<R> Labeled(String label, SD<R> s) {
		return null;
	}

	public SD<R> While(ED<Boolean> e, SD<R> s) {
		return (rho, sigma, brk, contin, err) -> {
			new K0() {
				@Override
				public void call() {
					If(e, Seq2(s, (a, b, c, d, e) -> call())).accept(rho, sigma, brk, contin, err);
				}
			}.call();
		};
	}

	public SD<R> DoWhile(SD<R> s, ED<Boolean> e) {
		return Seq2(s, While(e, s));
	}

	/*
	 * TODO: Switch - the fields are bad, and should not be needed (e.g.
	 * defaultFound, data etc.) - break now only works within cases, but it
	 * should also work for while/for (use the brk continuation to deal with
	 * break) - you cannot use foreach over the cases, chain them. Start with
	 * the first, pass the tail as the normal continuation (e.g., when there’s
	 * no break). - it should also work for Strings, so use Object, not Integer
	 * - data.caseNumber should be avoided by having a ​*different*​ denotation
	 * for case/defaults
	 */
	SwitchContext data = new SwitchContext();

	public <S> SD<S> Switch(ED<Integer> expr, SD<S>... cases) {
		return (rho, sigma, brk, contin, err) -> expr.accept(x -> {
			data.caseNumber = x;
			Stream.of(cases).forEach(_case -> {
				if (!data.breakFound)
					_case.accept(rho, sigma, brk, contin, err);
			});
		} , err);
	}

	public SD<R> Case(ED<Integer> constant, SD<R> expStat) {
		return (rho, sigma, brk, contin, err) -> {
			constant.accept(r -> {
				if (r.equals(data.caseNumber)) {
					data.caseFound = true;
				}
				if (data.caseFound || data.defaultFound) {
					expStat.accept(rho, sigma, brk, contin, err);
				}
			} , err);
		};
	}

	public SD<R> Default(SD<R> expStat) {
		return (rho, sigma, brk, contin, err) -> {
			expStat.accept(rho, sigma, brk, contin, err);
			data.defaultFound = true;
		};
	}

	public SD<R> Break() {
		return (rho, sigma, brk, contin, err) -> {
			data.breakFound = true;
			brk.accept("");
		};
	}

	public SD<R> Break(String label) {
		return (rho, sigma, brk, contin, err) -> {
			data.breakFound = true;
			brk.accept(label);
		};
	}

	public SD<R> Continue() {
		return (rho, sigma, brk, contin, err) -> contin.call();
	}

	public SD<R> Continue(String label) {
		return null;
	}

	public SD<R> Return(ED<R> e) {
		return (rho, sigma, brk, contin, err) -> e.accept(rho, err);
	}

	public SD<R> Return() {
		return (rho, sigma, brk, contin, err) -> rho.accept(null);
	}

	public final SD<R> Seq(SD<R>... ss) {
		assert ss.length > 0;
		return Stream.of(ss).reduce(this::Seq2).get();
	}

	protected SD<R> Seq2(SD<R> s1, SD<R> s2) {
		return (rho, sigma, brk, contin, err) -> s1.accept(rho, () -> s2.accept(rho, sigma, brk, contin, err), brk,
				contin, err);
	}

	public <T extends Throwable> SD<R> Throw(ED<T> e) {
		return (rho, sigma, brk, contin, err) -> e.accept(r -> err.accept(r), err);
	}

	public <T extends Throwable> SD<R> TryCatch(SD<R> body, Class<T> type, Function<T, SD<R>> handle) {
		return (rho, sigma, brk, contin, err) -> {
			body.accept(rho, sigma, brk, contin, (Throwable exc) -> {
				if (type.isInstance(exc)) {
					handle.apply((T) exc).accept(rho, sigma, brk, contin, err);
				} else {
					err.accept(exc);
				}
			});
		};
	}

	public SD<R> TryFinally(SD<R> body, SD<R> fin) {
		return (rho, sigma, brk, contin, err) -> {
			body.accept(r -> {
				fin.accept(rho, () -> rho.accept(r), brk, contin, err);
			} , () -> {
			} , (s) -> {
			} , () -> {
				fin.accept(rho, sigma, brk, contin, err);
			} , (Throwable exc) -> {
				fin.accept(rho /* todo: exception here too */, () -> err.accept(exc), brk, contin, err);
			});
		};
	}

	public <U> SD<R> ExpStat(K0 thunk) {
		return (rho, sigma, brk, contin, err) -> {
			try {
				thunk.call();
			} catch (Throwable t) {
				err.accept(t);
				return;
			}
			sigma.call();
		};
	}

	/*
	 * HOAS for let expressions int x = 3; s ==> Let(Exp(3), x -> [[s]]) S Let(E
	 * exp, Function<E, S> body);
	 */
	public <U> SD<R> Decl(ED<U> exp, Function<U, SD<R>> body) {
		return (rho, sigma, brk, contin, err) -> exp.accept(r -> body.apply(r).accept(rho, sigma, brk, contin, err),
				err);
	}

	public <U> SD<R> For(ED<Iterable<U>> coll, Function<U, SD<R>> body) {
		return (rho, sigma, brk, contin, err) -> coll.accept(iterable -> {
			Iterator<U> iter = iterable.iterator();
			While((s, err2) -> s.accept(iter.hasNext()), Decl((s, err2) -> s.accept(iter.next()), body)).accept(rho,
					sigma, brk, contin, err);
		} , err);
	}
}
