package recaf.core;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class AbstractJavaImpl<R> implements AbstractJava<R> {
	
	protected R typePreserving(Cont<R> body) {
		Ref<R> result = new Ref<>();
		body.statementDenotation.accept(r -> { result.value = r; }, () -> {}, exc -> { throw new RuntimeException(exc); });
		return result.value;
	}
	
	public <T> Cont<T> Exp(Supplier<T> e) {
		return Cont.fromED((k, err) -> {
			T t = null;
			try {
				t = e.get();
			} catch (Throwable ex) {
				err.accept(ex);
				return;
			}
			k.accept(t);
		});
	}
	
	public Cont<R> Empty() {
		return Cont.fromSD((rho, sigma, err) -> sigma.call());
	}
	
	public Cont<R> If(Cont<Boolean> e, Cont<R> s) {
		return If(e, s, Empty());
	}

	public Cont<R> If(Cont<Boolean> e, Cont<R> s1, Cont<R> s2) {
		return Cont.fromSD((rho, sigma, err) -> e.expressionDenotation.accept(x -> {
			if (x) {
				s1.statementDenotation.accept(rho, sigma, err);
			} else {
				s2.statementDenotation.accept(rho, sigma, err);
			}
		} , err));
	}

	public Cont<R> Labeled(String label, Cont<R> s) {
		return null;
	}
		
	public Cont<R> While(Cont<Boolean> e, Cont<R> s) {
		return Cont.fromSD((rho, sigma, err) -> {
			new K0() {
				@Override
				public void call() {
					If(e, Seq2(s, Cont.<R> fromSD((a, b, c) -> call()))).statementDenotation.accept(rho, sigma, err);
				}
			}.call();
		});
	}
	
	public Cont<R> DoWhile(Cont<R> s, Cont<Boolean> e) {
		return Seq2(s, While(e, s));
	}
//		
//	public Cont<R> Switch(Cont<Boolean> s, ){
//		return Cont.fromSD((rho, sigma, err) -> s.expressionDenotation.accept(x -> {
//			if (x) {
//				s1.statementDenotation.accept(rho, sigma, err);
//			} else if (){
//				s2.statementDenotation.accept(rho, sigma, err);
//			}
//		} , err));
//		
//	}

	public Cont<R> Break() {
		return null;
	}

	public Cont<R> Break(String label) {
		return null;
	}

	public Cont<R> Continue() {
		return null;
	}

	public Cont<R> Continue(String label) {
		return null;
	}

	public Cont<R> Return(Cont<R> e) {
		return Cont.fromSD((rho, sigma, err) -> e.expressionDenotation.accept(rho, err));
	}
	
	public Cont<R> Return() {
		return Cont.fromSD((rho, sigma, err) -> rho.accept(null));
	}

	public final Cont<R> Seq(Cont<R>... ss) {
		assert ss.length > 0;
		return Stream.of(ss).reduce(this::Seq2).get();
	}

	protected Cont<R> Seq2(Cont<R> s1, Cont<R> s2) {
		return Cont.fromSD((rho, sigma, err) -> s1.statementDenotation.accept(rho,
				() -> s2.statementDenotation.accept(rho, sigma, err), err));
	}

	public <T extends Throwable> Cont<R> Throw(Cont<T> e) {
		return Cont.fromSD((rho, sigma, err) -> e.expressionDenotation.accept(r -> err.accept(r), err));
	}

	public <T extends Throwable> Cont<R> TryCatch(Cont<R> body, Class<T> type, Function<T, Cont<R>> handle) {
		return Cont.fromSD((rho, sigma, err) -> {
			body.statementDenotation.accept(rho, sigma, (Throwable exc) -> {
				if (type.isInstance(exc)) {
					handle.apply((T) exc).statementDenotation.accept(rho, sigma, err);
				} else {
					err.accept(exc);
				}
			});
		});
	}

	public Cont<R> TryFinally(Cont<R> body, SD<R> fin) {
		return Cont.fromSD((rho, sigma, err) -> {
			body.statementDenotation.accept(r -> {
				fin.accept(rho, () -> rho.accept(r), err);
			} , () -> {
				fin.accept(rho, sigma, err);
			} , (Throwable exc) -> {
				fin.accept(rho /* todo: exception here too */, () -> err.accept(exc), err);
			});
		});
	}

	public <U> Cont<R> ExpStat(Cont<U> e) {
		return Cont.fromSD((rho, sigma, err) -> e.expressionDenotation.accept(ignored -> sigma.call(), err));
	}
	
	/* HOAS for let expressions
	 * int x = 3; s ==> Let(Exp(3), x -> [[s]])
	 * S Let(E exp, Function<E, S> body);
	 * */
	public <U> Cont<R> Decl(Cont<U> exp, Function<U, Cont<R>> body) {
		return Cont.fromSD((rho, sigma, err) -> exp.expressionDenotation
				.accept(r -> body.apply(r).statementDenotation.accept(rho, sigma, err), err));
	}

	public <U> Cont<R> For(Cont<Iterable<U>> coll, Function<U, Cont<R>> body) {
		return Cont.fromSD((rho, sigma, err) -> coll.expressionDenotation.accept(iterable -> {
			Iterator<U> iter = iterable.iterator();
			While(Cont.fromED((s, err2) -> s.accept(iter.hasNext())),
					Decl(Cont.fromED((s, err2) -> s.accept(iter.next())), body)).statementDenotation.accept(rho, sigma,
							err);
		} , err));
	}
}
