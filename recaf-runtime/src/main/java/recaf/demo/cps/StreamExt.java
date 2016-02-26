package recaf.demo.cps;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.Ref;
import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.SD;
import recaf.core.cps.StmtJava;
import recaf.demo.cps.stream.Stream;
import rx.Observable;
import rx.Subscriber;

public class StreamExt<R> implements StmtJava<R>, JavaMethodAlg<Observable<R>, SD<R>> {
	
	Observable<R> result;
	public Observable<R> Method(SD<R> body) {
		
		result = Observable.create(subscriber -> {
			body.accept(null,
					r ->     { }, 
					() ->    { },
					label -> { },
					label -> { },
					ex ->    { subscriber.onError(ex); });
		});
		
		return result;
	}
	
	public <T> SD<R> Await(Supplier<CompletableFuture<T>> e, Function<T, SD<R>> body) {
		return (label, rho, sigma, brk, contin, err) -> get(e).accept(f -> {
			f.whenComplete((a, ex) -> {
				if (a == null) {
					err.accept(ex);
				} else {
					body.apply(a).accept(null, rho, sigma, brk, contin, err);
				}
			});
		} , err);
	}
	
	public <T> SD<R> AwaitFor(Supplier<Observable<R>> coll, Function<Ref<R>, SD<R>> body){
		return null;
	}
	
	public <U> SD<R> Yield(Supplier<U> exp) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(exp).accept(v -> {
				// result.doOnNext(v);
			} , err);
		};
	}

	public <U> SD<R> YieldFrom(Supplier<Observable<U>> exp) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(exp).accept(v -> {
				// result.mergeWith(v);
			} , err);
		};
	}
}
