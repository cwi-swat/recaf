import recaf.iter.Iter;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.*;
 
public class TestIterFib {

  static Void println(Object o) {
    System.out.println(o);
    return null;
  }
  
  class Cell {
  	public Integer v;
  	public Cell(Integer v) { this.v = v; }
  }
  
  Iterable<Integer> fib() {
  Iter<Integer> $alg = new Iter<Integer>();
  return (Iterable<Integer>)$alg.Method($alg.<Cell >Decl($alg.Exp(() -> { return new Cell(0); }), a -> {return $alg.<Cell >Decl($alg.Exp(() -> { return new Cell(1); }), b -> {return $alg.While($alg.Exp(() -> { return true; }), $alg.Seq($alg.Yield($alg.Exp(() -> { return a.v; })), $alg.Seq($alg.ExpStat($alg.Exp(() -> { return b.v = a.v + b.v; })), $alg.ExpStat($alg.Exp(() -> { return a.v = b.v - a.v; })))));});}));
} 
  
  public static void main(String args[]) {
    for (Integer n: new TestIterFib().fib()) {
      if(n > 100) break;
      println(n);
    }
  }
}