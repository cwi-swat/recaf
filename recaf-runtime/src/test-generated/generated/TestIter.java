package generated;
import recaf.demo.cps.Iter; 
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.function.Predicate;

public class TestIter {
  private static Iter<Integer> alg = new Iter<Integer>();  
  
  Iterable<Integer> range(int n) {
    return new Iterable<Integer>() {
       public Iterator<Integer> iterator() {
         return IntStream.range(0, n).iterator();
       }
    };
  }

    Iterable<Integer> filter(Iterable<Integer> iter, Predicate<Integer> pred) {
  recaf.core.Ref<Iterable<Integer>> $iter = new recaf.core.Ref<Iterable<Integer>>(iter); recaf.core.Ref<Predicate<Integer>> $pred = new recaf.core.Ref<Predicate<Integer>>(pred);
  return (Iterable<Integer>)alg.Method(alg.<Integer >ForEach(() -> $iter.value, (recaf.core.Ref<Integer > t) -> alg.If(() -> $pred.value.test(t.value), alg.Yield(() -> t.value))));
}

    Iterable<Integer> subIter() {
  return (Iterable<Integer>)alg.Method(alg.<Integer >ForEach(() -> range(10), (recaf.core.Ref<Integer > i) -> alg.Yield(() -> i.value)));
}

    Iterable<Integer> myIter() {
  return (Iterable<Integer>)alg.Method(alg.While(() -> true, alg.YieldFrom(() -> filter(subIter(), x -> ((Integer)x) % 2 == 0))));
}
  
  public static void main(String args[]) {
    for (Integer i: new TestIter().myIter()) {
      System.out.println("i = " + i);
    }
  }

}
