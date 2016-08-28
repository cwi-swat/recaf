package generated;

import recaf.demo.constraint.Solve;
import java.util.Map;
import org.chocosolver.solver.variables.IntVar;

public class TestSolve_alldifferent {
  private static Solve  alg = new Solve();
  
    Iterable<Map<String,Integer>> example() {
  return (Iterable<Map<String,Integer>>)alg.Method(alg.Var(alg.Lit(0), alg.Lit(1), (IntVar w) -> { return alg.Var(alg.Minus(alg.Lit(1)), alg.Lit(2), (IntVar x) -> { return alg.Var(alg.Lit(2), alg.Lit(4), (IntVar y) -> { return alg.Var(alg.Lit(5), alg.Lit(7), (IntVar z) -> { return alg.Solve(alg.Invoke(alg.This(this), "alldifferent", alg.Var("w", w), alg.Var("x", x), alg.Var("y", y), alg.Var("z", z))); }); }); }); }));
}

  public static void main(String args[]) {
    for (Map<String,Integer> sol: new TestSolve_alldifferent().example()) {
      System.out.println(sol);
    } 
  }
}