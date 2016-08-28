package generated;

import recaf.core.full.FullJavaDirect;


public class TestDef {
   private static FullJavaDirect<Void> alg = new FullJavaDirect<Void>() {};
   
   static  void doSomething() {
  alg.Method(alg.Decl(() -> 0, (recaf.core.Ref<Integer> sum) -> {return alg.Seq(alg.ForDecl(() -> 0, (recaf.core.Ref<Integer> i) -> alg.ForBody(() -> i.value < 10, alg.Seq(alg.ExpStat(() -> { i.value++; return null; }), alg.Empty()), alg.If(() -> i.value % 2 == 0, alg.ExpStat(() -> { sum.value += i.value; return null; })))), alg.ExpStat(() -> { System.out.println("sum = " + sum.value); return null; }));}));
}
  
   public static void main(String args[]) {
      doSomething();
   }

}