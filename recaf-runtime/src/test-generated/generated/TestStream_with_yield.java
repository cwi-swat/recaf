package generated;
import java.util.concurrent.ExecutionException;

import recaf.demo.cps.StreamExt;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class TestStream_with_yield {
  
  private StreamExt<Integer> alg = new StreamExt<Integer>();
 
    Observable<Integer> simpleStream() {
  return (Observable<Integer>)alg.Method(alg.Seq(alg.Yield(() -> 1), alg.Seq(alg.Yield(() -> 2), alg.Seq(alg.Yield(() -> 3), alg.Seq(alg.Yield(() -> 4), alg.Yield(() -> 5))))));
}  
  
  public static void main(String args[]) throws InterruptedException, ExecutionException {
	Subscription sub = new TestStream_with_yield().simpleStream().subscribe(new Action1<Integer>() {
        @Override
        public void call(Integer s) {
              System.out.println(s);
        }
    });
  }
}
