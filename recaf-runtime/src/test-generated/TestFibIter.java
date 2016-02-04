import recaf.core.Ref;
import recaf.iter.Iter;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.*;

public class TestFibIter {

  static Void println(Object o) {
    System.out.println(o);
    return null;
  }
  
  Iterable<Integer> fib() {
  Iter<Integer> $alg = new Iter<Integer>();
  return (Iterable<Integer>)$alg.Method($alg.<Integer>Decl($alg.Exp(() -> { return 0; }), a -> {return $alg.<Integer>Decl($alg.Exp(() -> { return 1; }), b -> {return $alg.While($alg.Exp(() -> { return true; }), $alg.Seq($alg.Yield($alg.Exp(() -> { return a; })), $alg.Seq($alg.ExpStat($alg.Exp(() -> { return b = a + b; })), $alg.ExpStat($alg.Exp(() -> { return a = b - a; })))));});}));
} 
  
  public static void main(String args[]) {
    int count = 10;
    for (Integer n: new TestFibIter().fib()) {
      if(count-- == 0) {
        println(n);
        break;
      }
    }
  }
}