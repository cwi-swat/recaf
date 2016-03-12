package recaf.demo.cps;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.core.ISupply;
import recaf.core.Ref;
import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.SD;
import recaf.core.cps.StmtJava;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

public class StreamExt<R> implements StmtJava<R>, JavaMethodAlg<Subject<R, R>, SD<R>> {
	
	ReplaySubject<R> result = ReplaySubject.create();

	public Subject<R, R> Method(SD<R> body) {
		body.accept(null,
				r ->     { result.onNext(r); }, 
				() ->    {  },
				label -> { },
				label -> { },
				ex ->    { result.onError(ex);});
		
		return result;
	}
	
	public <T> SD<R> Await(ISupply<CompletableFuture<T>> e, Function<T, SD<R>> body) {
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
	
	public SD<R> Yield(ISupply<R> exp) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(exp).accept(v -> { 
				result.onNext(v);
				sigma.call();
			} , err);
		};
	}
	
	public <T> SD<R> YieldFrom(ISupply<Observable<R>> exp) {
		return (label, rho, sigma, brk, contin, err) -> {
			get(exp).accept(v -> { 
				result.mergeWith(v);
				sigma.call();
			} , err);
		};
	}
	
	public <T> SD<R> AwaitFor(Supplier<Observable<R>> coll, Function<T, SD<R>> body){
		throw new UnsupportedOperationException();
	}
}
