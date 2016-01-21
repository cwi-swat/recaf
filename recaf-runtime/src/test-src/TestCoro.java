
import recaf.coro.CoroutineExtension;
import java.util.stream.IntStream;
import java.util.Iterator;

public class TestCoro {

  static Void println(Object o) {
    System.out.println(o);
    return null;
  }
  
  Iterable<Integer> range(int n) {
    return new Iterable<Integer>() {
       public Iterator<Integer> iterator() {
         return IntStream.range(0, n).iterator();
       }
    };
  }

Iterable<Integer> subCoro(CoroutineExtension<Integer> alg) {
  return (Iterable<Integer>)alg.Method(alg.For(alg.Exp(() -> { return range(10); }), (Integer i) -> {return alg.Yield(alg.Exp(() -> { return i; }));}));
}

  Iterable<Integer> coro(CoroutineExtension<Integer> alg) {
  return (Iterable<Integer>)alg.Method(alg.While(alg.Exp(() -> { return true; }), alg.YieldFrom(alg.Exp(() -> { return subCoro(alg); }))));
}
  
  public static void main(String args[]) {
    for (Integer i: new TestCoro().coro(new CoroutineExtension<Integer>())) {
      System.out.println("i = " + i);
    }
  }

}