package generated;

import recaf.demo.cps.Iter;

public class TestFibIter {
  private static Iter<Integer> alg = new Iter<Integer>();

  private static  Iterable<Integer> fib() {
  return (Iterable<Integer>)alg.Method(alg.Decl(() -> 0, (recaf.core.Ref<Integer> a) -> {return alg.Decl(() -> 1, (recaf.core.Ref<Integer> b) -> {return alg.While(() -> true, alg.Seq(alg.Yield(() -> a.value), alg.Seq(alg.ExpStat(() -> { b.value = a.value + b.value; return null; }), alg.ExpStat(() -> { a.value = b.value - a.value; return null; }))));});}));
} 
  
  public static void main(String args[]) {
    int count = 10;
    for (Integer n: fib()) {
      if(count-- == 0) {
        System.out.println(n);
        break;
      }
    }
  }
}