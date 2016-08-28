package generated;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import recaf.demo.cps.Async;
import recaf.demo.cps.StreamExt;
import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber; 

public class TestStream_Complex {

     CompletableFuture<Integer> accumulateAsync(Async<Integer> alg, Integer state, Integer next) {
  recaf.core.Ref<Integer > $state = new recaf.core.Ref<Integer >(state); recaf.core.Ref<Integer > $next = new recaf.core.Ref<Integer >(next);
  return (CompletableFuture<Integer>)alg.Method(alg.Return(() -> $state.value + $next.value));
}  
 
     Observable<Integer> scan(StreamExt<Integer> alg, Observable<Integer> src, 
   						    Integer state) {
  recaf.core.Ref<Observable<Integer>> $src = new recaf.core.Ref<Observable<Integer>>(src); recaf.core.Ref<Integer > $state = new recaf.core.Ref<Integer >(state);
  return (Observable<Integer>)alg.Method(alg.Seq(alg.Yield(() -> $state.value), alg.AwaitFor(() -> $src.value, (Integer next) -> {return alg.Await(() -> accumulateAsync(new Async<Integer>(){}, $state.value, next), (Integer x) -> { return alg.Seq(alg.Yield(() -> x), alg.ExpStat(() -> { $state.value = x; return null; })); });})));
}
  
    Observable<Integer> simpleStream(StreamExt<Integer> alg) {
  return (Observable<Integer>)alg.Method(alg.Seq(alg.Yield(() -> 1), alg.Seq(alg.Yield(() -> 2), alg.Seq(alg.Yield(() -> 3), alg.Seq(alg.Yield(() -> 4), alg.Yield(() -> 5))))));
} 
  
  public static void main(String args[]) throws InterruptedException, ExecutionException {	  
		TestSubscriber<Integer> testSubscriber = new TestSubscriber<Integer>();

		Subscription sub = new TestStream_Complex()
				.scan(new StreamExt<Integer>(), new TestStream_Complex().simpleStream(new StreamExt<Integer>()), 0)
				.take(6)
				.subscribe(testSubscriber);

		testSubscriber.awaitTerminalEvent();
		List<Integer> ret = testSubscriber.getOnNextEvents();
		ret.forEach(new Consumer<Integer>() {
			@Override
			public void accept(Integer t) {
				System.out.println(t);
			}
		});
  }
}
