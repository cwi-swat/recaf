package generated;

import java.util.List;
import recaf.demo.cps.Backtrack;
import static java.util.Arrays.asList;
import java.util.stream.IntStream;
import java.util.Iterator;

public class TestBacktracking {

  static class Pair {
    int x, y;
    Pair(int x, int y) {
      this.x = x;
      this.y = y;
    }
    public String toString() {
      return "(" + x + ", " + y + ")";
    }
  }

  static  List<Pair> solve(Backtrack<Pair> alg) {
  return (List<Pair>)alg.Method(alg.Choose(() -> asList(1, 2, 3), (x) -> { return alg.Seq(alg.ExpStat(() -> { System.err.println("Choice 1: " + x); return null; }), alg.Choose(() -> asList(4, 5, 6), (y) -> { return alg.Seq(alg.ExpStat(() -> { System.err.println("Choice 2: " + y); return null; }), alg.If(() -> x * y == 8, alg.Return(() -> new Pair(x, y)))); })); }));
}
  
  static Iterable<Integer> range(int n) {
    return new Iterable<Integer>() {
       public Iterator<Integer> iterator() {
         return IntStream.range(0, n).iterator();
       }
    };
  }
  
  public static void main(String args[]) {
    System.out.println(solve(new Backtrack<Pair>()));
  }
  

}