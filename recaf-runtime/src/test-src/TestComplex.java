 
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import recaf.async.AsyncExtension;
 
public class TestComplex {
	
	CompletableFuture<Integer> op(AsyncExtension<Integer> alg) {
  AsyncExtension<Integer> $alg = alg;   
  return (CompletableFuture<Integer>)$alg.Method($alg.Seq($alg.If($alg.Exp(() -> { return 1 < 2; }), $alg.Return($alg.Exp(() -> { return 42; }))), $alg.Return($alg.Exp(() -> { return 41; }))));
}
  	
	CompletableFuture<Integer> op2(AsyncExtension<Integer> alg) {
  AsyncExtension<Integer> $alg = alg;   
  return (CompletableFuture<Integer>)$alg.Method($alg.Seq($alg.If($alg.Exp(() -> { return 1 < 2; }), $alg.Await($alg.Exp(() -> { return op(alg); }), (Integer x) -> {return $alg.Await($alg.Exp(() -> { return op(alg); }), (Integer y) -> {return $alg.Return($alg.Exp(() -> { return x + y; }));});})), $alg.Return($alg.Exp(() -> { return 41; }))));
}
  	
  	public static void main(String[] args) throws InterruptedException, ExecutionException{
  		Integer answer = new TestComplex().op2(new AsyncExtension<Integer>()).get();
  		
  		System.out.println(answer);
  	}
}