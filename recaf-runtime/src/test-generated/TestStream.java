import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import recaf.stream.Stream;
import recaf.stream.StreamExtension;

public class TestStream {

  static Void println(Object o) {
    System.out.println(o);
    return null;
  }
  
  Stream<Integer> asyncRange() {
  StreamExtension<Integer> $alg = new StreamExtension<Integer>();
  return (Stream<Integer>)$alg.Method($alg.Seq($alg.Yield($alg.Exp(() -> { return 1; })), $alg.Seq($alg.Yield($alg.Exp(() -> { return 2; })), $alg.Seq($alg.Yield($alg.Exp(() -> { return 3; })), $alg.Seq($alg.Yield($alg.Exp(() -> { return 4; })), $alg.Yield($alg.Exp(() -> { return 5; })))))));
} 
  
  public static void main(String args[]) throws InterruptedException, ExecutionException {
	Stream i = new TestStream().asyncRange();
  }

}
