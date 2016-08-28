package generated;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import recaf.demo.cps.Async;

public class TestFibRecAsync {
  private static Async<Integer> alg = new Async<Integer>() {};
  
    CompletableFuture<Integer> fib(Integer x) {
  recaf.core.Ref<Integer > $x = new recaf.core.Ref<Integer >(x);
  return (CompletableFuture<Integer>)alg.Method(alg.If(() -> $x.value < 2, alg.Return(() -> $x.value), alg.Await(() -> fib($x.value-1), (x1) -> { return alg.Await(() -> fib($x.value-2), (x2) -> { return alg.Return(() -> x1 + x2); }); })));
} 
  
  public static void main(String[] args) throws InterruptedException, ExecutionException{
	CompletableFuture<Integer> answer;
			
	answer = new TestFibRecAsync().fib(10);
	
	System.out.println(answer.get());
  }
}