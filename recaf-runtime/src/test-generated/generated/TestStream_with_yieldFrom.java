package generated;
import java.util.concurrent.ExecutionException;

import recaf.demo.cps.StreamExt;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class TestStream_with_yieldFrom {
 
    private StreamExt<Integer> alg = new StreamExt<Integer>();
 
	  Observable<Integer> simpleStream() {
  return (Observable<Integer>)alg.Method(alg.Seq(alg.Yield(() -> 1), alg.Seq(alg.Yield(() -> 2), alg.Seq(alg.Yield(() -> 3), alg.Seq(alg.YieldFrom(() -> simpleStream2()), alg.Seq(alg.Yield(() -> 4), alg.Yield(() -> 5)))))));
}  
  
	  Observable<Integer> simpleStream2() {
  return (Observable<Integer>)alg.Method(alg.Seq(alg.Yield(() -> 11), alg.Seq(alg.Yield(() -> 22), alg.Seq(alg.Yield(() -> 33), alg.Seq(alg.Yield(() -> 44), alg.Yield(() -> 55))))));
}
  
  public static void main(String args[]) throws InterruptedException, ExecutionException {
	Subscription sub = new TestStream_with_yieldFrom().simpleStream().subscribe(new Action1<Integer>() {
        @Override
        public void call(Integer s) {
              System.out.println(s);
        }
    });
  }
}
