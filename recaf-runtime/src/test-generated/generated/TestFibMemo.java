package generated;

import recaf.demo.direct.Memo;

public class TestFibMemo {
  private static Memo<Integer> alg = new Memo<Integer>();

  private  Integer  fib(Integer x) {
  recaf.core.Ref<Integer > $x = new recaf.core.Ref<Integer >(x);
  return (Integer )alg.Method(alg.If(alg.Lt(alg.Ref("$x", $x), alg.Lit(2)), alg.Return(alg.Ref("$x", $x)), alg.Decl(alg.Invoke(alg.This(this), "fib", alg.Minus(alg.Ref("$x", $x), alg.Lit(1))), (recaf.core.Ref<Integer > x1) -> {return alg.Decl(alg.Invoke(alg.This(this), "fib", alg.Minus(alg.Ref("$x", $x), alg.Lit(2))), (recaf.core.Ref<Integer > x2) -> {return alg.Return(alg.Plus(alg.Ref("x1", x1), alg.Ref("x2", x2)));});})));
} 
  
  public static void main(String args[]) {
    int count = 10;
    TestFibMemo t = new TestFibMemo();
   	System.out.println(t.fib(12));
   	System.out.println(t.fib(12));
  }
}