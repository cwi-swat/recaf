package generated;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import recaf.demo.cps.StreamExt;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

public class TestStream_with_awaitFor {
	
    private StreamExt<Integer> alg = new StreamExt<Integer>();
  
 	  Observable<Integer> simpleStream() {
  return (Observable<Integer>)alg.Method(alg.Seq(alg.Yield(() -> 1), alg.Seq(alg.Yield(() -> 2), alg.Seq(alg.Yield(() -> 3), alg.Seq(alg.Yield(() -> 4), alg.Yield(() -> 5))))));
}  
	  
	  Observable<Integer> simpleStream2(Observable<Integer> stream) {
  recaf.core.Ref<Observable<Integer>> $stream = new recaf.core.Ref<Observable<Integer>>(stream);
  return (Observable<Integer>)alg.Method(alg.Seq(alg.Yield(() -> 1), alg.AwaitFor(() -> $stream.value, (Integer next) -> {return alg.Await(() -> CompletableFuture.supplyAsync(()->42), (Integer x) -> { return alg.Yield(() -> x); });})));
} 
  
    public static void main(String args[]) throws InterruptedException, ExecutionException {
      CountDownLatch latch = new CountDownLatch(1);
    
	  Subscription sub = new TestStream_with_awaitFor()
	  							.simpleStream2(new TestStream_with_awaitFor().simpleStream())
	  							.subscribe(new Action1<Integer>() {
							        @Override
							        public void call(Integer s) {
							              System.out.println(s);
							        }
        						}, 
        						new Action1<Throwable>() {
							        @Override
							        public void call(Throwable e) {
							              latch.countDown();
							        }
        						}
        						,
        						new Action0() {
							        @Override
							        public void call() {
							              latch.countDown();
							        }
        						});
        						
       latch.await();
    }
}
