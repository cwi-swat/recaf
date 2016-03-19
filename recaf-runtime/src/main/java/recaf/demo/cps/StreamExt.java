package recaf.demo.cps;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import recaf.core.ISupply;
import recaf.core.alg.JavaMethodAlg;
import recaf.core.cps.EvalJavaStmt;
import recaf.core.cps.K;
import recaf.core.cps.K0;
import recaf.core.cps.SD;
import rx.Observable;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

public class StreamExt<R> implements EvalJavaStmt<R>, JavaMethodAlg<Subject<R, R>, SD<R>> {
	
	ReplaySubject<R> result = ReplaySubject.create();
	int depth = 0; // light depth tracking
	
	public Subject<R, R> Method(SD<R> body) {
		depth++;
		body.accept(null,
				r -> { 
					depth--;
					if (depth==0) result.onCompleted(); 
				}, 
				() -> { 
					depth--;
					if (depth==0) result.onCompleted(); 
				},
				label -> { },
				label -> { },
				ex ->    { result.onError(ex);  }) ;
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
				sigma.call();		
			} , err);
		};
	}
	
	private <T> void loop(Iterator<R> iter, Function<R, SD<T>> body, K<T> rho, K0 sigma, K<String> brk, K<String> contin, K<Throwable> err){
		body.apply(iter.next()).accept(null,
		   rho, 
		   () -> {
			   if (iter.hasNext())
				   loop(iter, body, rho, sigma, brk, contin, err);
			   else
				   sigma.call();
		   }, brk, contin, err);
	}
	
	public <T> SD<T> AwaitFor(ISupply<Observable<R>> coll, Function<R, SD<T>> body){
		return (label, rho, sigma, brk, contin, err) -> {
			get(coll).accept(v -> { 
				
				Iterator<R> it = v.toBlocking().toIterable().iterator();
				
				loop(it, body, rho, sigma, brk, contin, err);
				
			} , err);
		};
	}
}
