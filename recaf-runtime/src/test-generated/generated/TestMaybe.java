package generated;
import recaf.demo.cps.Maybe;
import java.util.Optional;
 
public class TestMaybe {
  private Maybe<Integer> alg = new Maybe<Integer>();
  
    Optional<Integer> maybe() {
  return (Optional<Integer>)alg.Method(alg.Decl(() -> 3, (recaf.core.Ref<Integer> x) -> {return alg.Seq(alg.ExpStat(() -> { System.out.println("maybe"); return null; }), alg.If(() -> 7 > 5, alg.Seq(alg.ExpStat(() -> { System.out.println("Yes"); return null; }), alg.Return(() -> 42))));}));
}
  
    Optional<Integer> readOptional() {
  return (Optional<Integer>)alg.Method(alg.Maybe(() -> maybe(), (x) -> { return alg.Return(() -> x + 1); }));
}
  
  public static void main(String args[]) {
	 Optional<Integer> x = new TestMaybe().readOptional();
	 System.out.println(x.get());
  }
  
}