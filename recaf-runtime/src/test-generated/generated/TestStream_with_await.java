package generated;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import recaf.demo.cps.Async;
import recaf.demo.cps.StreamExt;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

public class TestStream_with_await {
   
    CompletableFuture<Integer> secondOp(Async<Integer> alg) {
  return (CompletableFuture<Integer>)alg.Method(alg.Seq(alg.If(() -> 1 < 2, alg.Return(() -> 42)), alg.Return(() -> 41)));
}
  
    Observable<Integer> simpleStream2(StreamExt<Integer> alg) {
  return (Observable<Integer>)alg.Method(alg.Seq(alg.Yield(() -> 1), alg.Seq(alg.Yield(() -> 2), alg.Await(() -> secondOp(new Async<Integer>(){}), (Integer x) -> { return alg.Seq(alg.Yield(() -> x), alg.Seq(alg.Yield(() -> 4), alg.Yield(() -> 5))); }))));
} 
  
  public static void main(String args[]) throws InterruptedException, ExecutionException {
    CountDownLatch latch = new CountDownLatch(1);
  
	Subscription sub = new TestStream_with_await()
								.simpleStream2(new StreamExt<Integer>())
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
