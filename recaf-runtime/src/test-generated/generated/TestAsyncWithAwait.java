package generated;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import recaf.demo.cps.Async;

public class TestAsyncWithAwait {
   private static Async<Integer> alg = new Async<Integer>() {};

      CompletableFuture<Integer> op() {
  return (CompletableFuture<Integer>)alg.Method(alg.Seq(alg.If(() -> 1 < 2, alg.Await(() -> secondOp(), (x) -> { return alg.Return(() -> x); })), alg.Return(() -> 41)));
} 
  	
  	  CompletableFuture<Integer> secondOp() {
  return (CompletableFuture<Integer>)alg.Method(alg.Seq(alg.If(() -> 1 < 2, alg.Return(() -> 42)), alg.Return(() -> 41)));
}
  	
  	public static void main(String[] args) throws InterruptedException, ExecutionException{
  		Integer answer = new TestAsyncWithAwait().op().get();
  		
  		System.out.println(answer);
  	}
}