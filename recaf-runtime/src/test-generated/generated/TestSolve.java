package generated;

import recaf.demo.constraint.Solve;
import java.util.Map;
import org.chocosolver.solver.variables.IntVar;

public class TestSolve {
  private static Solve  alg = new Solve();
  
    Iterable<Map<String,Integer>> example() {
  return (Iterable<Map<String,Integer>>)alg.Method(alg.Var(alg.Lit(0), alg.Lit(5), (IntVar x) -> { return alg.Var(alg.Lit(0), alg.Lit(5), (IntVar y) -> { return alg.Seq(alg.Solve(alg.Lt(alg.Plus(alg.Var("x", x), alg.Var("y", y)), alg.Lit(5))), alg.Solve(alg.Gt(alg.Minus(alg.Var("x", x), alg.Var("y", y)), alg.Lit(1)))); }); }));
}

  public static void main(String args[]) {
    for (Map<String,Integer> sol: new TestSolve().example()) {
      System.out.println(sol);
    } 
  }
}