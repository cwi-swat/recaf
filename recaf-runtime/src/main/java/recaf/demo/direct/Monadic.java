//package recaf.demo.direct;
//
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//import recaf.core.ISupply;
//import recaf.core.alg.JavaMethodAlg;
//import recaf.core.direct.IExec;
//import recaf.core.direct.StmtJavaDirect;
//
//interface Monad<M, A> extends App<M, A> {
//	App<M, A> unit(A r);
//	<B> B bind(App<M, A> x, Function<A, B> f);
//}
//
//class Opt<T> implements Monad<Opt.t, T> {
//	class t {}
//
//	private T value;
//
//	public Opt(T value) {
//		this.value = value;
//	}
//
//	@Override
//	public App<Opt.t, T> unit(T r) {
//		return new Opt<T>(r);
//	}
//
//	@Override
//	public <B> B bind(App<t, T> x, Function<T, B> f) {
//		if (value != null) {
//			return f.apply(value);
//		}
//		return new Opt<>(null);
//	}
//	
//}
//
//public abstract class Monadic<M, R> implements StmtJavaDirect<R>, JavaMethodAlg<App<M, R>, IExec> {
//
//	abstract Monad<M, R> monad();
//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public App<M, R> Method(IExec body) {
//		try {
//			body.exec(null);
//		} catch (Return r) {
//			return Optional.of((R) r.getValue());
//		} catch (Throwable e) {
//			throw new RuntimeException(e);
//		}
//		return Optional.empty();
//	}
//
//	public <T> IExec Let(Supplier<App<M, T>> m, Function<T, IExec> body) {
//		return l -> {
//			m.bind(body);
////			Optional<U> v = opt.get();
////			if (v.isPresent()) {
////				body.apply(v.get()).exec(null);
////			}
//		};
//	}
//	
//	@Override
//	public IExec Return(ISupply<R> e) {
//		return l -> {
//			throw new Return(monad().unit(e.get()));
//		};
//		//return StmtJavaDirect.super.Return(e);
//	}
//
//}
