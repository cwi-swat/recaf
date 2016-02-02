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
  return (Iterable<Integer>)$alg.Method($alg.<Ref >Decl($alg.Exp(() -> { return new Ref(); }), a -> {return $alg.Seq($alg.ExpStat($alg.Exp(() -> { return a.value = 0; })), $alg.<Ref >Decl($alg.Exp(() -> { return new Ref(); }), b -> {return $alg.Seq($alg.ExpStat($alg.Exp(() -> { return b.value = 1; })), $alg.While($alg.Exp(() -> { return true; }), $alg.Seq($alg.Yield($alg.Exp(() -> { return a.value; })), $alg.Seq($alg.ExpStat($alg.Exp(() -> { return b.value = (Integer)a.value + (Integer)b.value; })), $alg.ExpStat($alg.Exp(() -> { return a.value = (Integer)b.value - (Integer)a.value; }))))));}));}));
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