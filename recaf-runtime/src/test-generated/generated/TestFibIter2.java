package generated;

import recaf.demo.cps.IterFull;

public class TestFibIter2 {
  private static IterFull<Integer> alg = new IterFull<Integer>() {};

  private static  Iterable<Integer> fib() {
  return (Iterable<Integer>)alg.Method(alg.Decl(alg.Lit(0), (recaf.core.Ref<Integer> a) -> {return alg.Decl(alg.Lit(1), (recaf.core.Ref<Integer> b) -> {return alg.While(alg.Lit(true), alg.Seq(alg.Yield(alg.Ref("a", a)), alg.Seq(alg.ExpStat(alg.Assign(alg.Ref("b", b), alg.Plus(alg.Ref("a", a), alg.Ref("b", b)))), alg.ExpStat(alg.Assign(alg.Ref("a", a), alg.Minus(alg.Ref("b", b), alg.Ref("a", a)))))));});}));
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