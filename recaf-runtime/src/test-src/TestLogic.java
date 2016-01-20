import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import recaf.amb.BacktrackingExtension;
import recaf.amb.Results;

public class TestLogic {

  static Iterable<Integer> range(int n) {
    return new Iterable<Integer>() {
       public Iterator<Integer> iterator() {
         return IntStream.range(0, n).iterator();
       }
    };
  }
  
  Void set(Integer[] arr, int index, int val) {
    arr[index] = val;
    return null;
  }

  Results<Integer[]> queens(BacktrackingExtension<Integer[]> alg) {
  return (Results<Integer[]>)alg.Method(alg.<Integer[]>Decl(alg.Exp(() -> { return new Integer[] {0, 0, 0, 0, 0, 0, 0, 0, 0}; }), board -> {return alg.Seq(alg.For(alg.Exp(() -> { return range(8); }), (Integer column) -> {return alg.Some(alg.Exp(() -> { return range(8); }), (Integer row) -> {return alg.Seq(alg.For(alg.Exp(() -> { return range(column); }), (Integer i) -> {return alg.Seq(alg.Check(alg.Exp(() -> { return board[i] != row; })), alg.Seq(alg.Check(alg.Exp(() -> { return board[i] != row + column - i; })), alg.Check(alg.Exp(() -> { return board[i] != row + 1 - column; }))));}), alg.ExpStat(alg.Exp(() -> { return set(board, column, row); })));});}), alg.Return(alg.Exp(() -> { return board; })));}));
}

  public static void main(String args[]) {
    BacktrackingExtension<Integer[]> bt = new BacktrackingExtension<Integer[]>();
    Results<Integer[]> r = new TestLogic().queens(bt);
    int j = 0;
    for (Integer[] sol: r) {
       for (Integer i: sol) {
         System.out.println("sol " + j + ": " + i);
       }
     }
  }
 

}