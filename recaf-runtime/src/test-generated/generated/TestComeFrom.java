package generated;

import recaf.demo.cps.ComeFrom;
import java.util.Scanner;

public class TestComeFrom {

  // from wikipedia
  static  Void  repl(ComeFrom<Void> cf) {
  return (Void )cf.Method(cf.Seq(cf.ComeFrom(() -> "l40"), cf.Seq(cf.ExpStat(() -> { System.out.println("What is your name?"); return null; }), cf.Decl(() -> new Scanner(System.in), (recaf.core.Ref<Scanner > scanner) -> {return cf.Decl(() -> scanner.value.next(), (recaf.core.Ref<String > name) -> {return cf.Seq(cf.ExpStat(() -> { System.out.println("Hello, " + name.value); return null; }), cf.Labeled("l40", cf.Empty()));});}))));
}
 
  public static void main(String[] args) {
        repl(new ComeFrom<Void>());
  }
}