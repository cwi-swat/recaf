package generated;
import recaf.demo.cps.Iter; 
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.function.Predicate;

public class TestIter_YieldEachRec {
  private static Iter<Integer> alg = new Iter<Integer>();  
  
    Iterable<Integer> naturalsDownFrom(int n) {
  recaf.core.Ref<Integer> $n = new recaf.core.Ref<Integer>(n);
  return (Iterable<Integer>)alg.Method(alg.If(() -> $n.value >0, alg.Seq(alg.Yield(() -> $n.value), alg.YieldFrom(() -> naturalsDownFrom($n.value-1)))));
}
  
  public static void main(String args[]) {
    for (Integer i: new TestIter_YieldEachRec().naturalsDownFrom(10)) {
      System.out.println("i = " + i);
    }
  }

}
