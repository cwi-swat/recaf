import recaf.iter.Iter;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import recaf.async.AsyncExtension;


public class TestFibRec {

  static Void println(Object o) {
    System.out.println(o);
    return null;
  }
  
  CompletableFuture<Integer> fib(Integer x) {
  AsyncExtension<Integer> $alg = new AsyncExtension<Integer>();
  return (CompletableFuture<Integer>)$alg.Method($alg.If($alg.Exp(() -> { return x < 2; }), $alg.Return($alg.Exp(() -> { return x; })), $alg.Await($alg.Exp(() -> { return fib(x-1); }), (Integer x1) -> { return $alg.Await($alg.Exp(() -> { return fib(x-2); }), (Integer x2) -> { return $alg.Return($alg.Exp(() -> { return x1 + x2; })); }); })));
} 
  
  public static void main(String[] args) throws InterruptedException, ExecutionException{
	CompletableFuture answer;
			
	answer = new TestFibRec().fib(10);
	
	System.out.println(answer.get());
  }
}