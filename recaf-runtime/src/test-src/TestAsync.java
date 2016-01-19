 
import java.util.concurrent.CompletableFuture;
import recaf.async.AsyncExtension;
 
public class TestAsync {
	
	CompletableFuture<Integer> op(AsyncExtension<Integer> alg) {
  AsyncExtension<Integer> $alg = alg;   
  return (CompletableFuture<Integer>)$alg.Method($alg.Seq($alg.If($alg.Exp(() -> { return 1 > 5; }), $alg.Return($alg.Exp(() -> { return 42; }))), $alg.Return($alg.Exp(() -> { return 41; }))));
}
}