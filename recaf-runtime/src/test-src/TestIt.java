
 
import java.util.Optional;

import recaf.maybe.MaybeExtension;
 
public class TestIt {
  

  Optional<Integer> maybe(MaybeExtension<Integer> alg) {
  MaybeExtension<Integer> $alg = alg;   
  return (Optional<Integer>)$alg.Method($alg.If($alg.Exp(() -> { return 1 > 5; }), $alg.Return($alg.Exp(() -> { return 42; }))));
}
  
  Optional<Integer> readOptional(MaybeExtension<Integer> alg) {
  MaybeExtension<Integer> $alg = alg;   
  return (Optional<Integer>)$alg.Method($alg.Maybe($alg.Exp(() -> { return maybe(alg); }), (Integer x) -> {return $alg.Return($alg.Exp(() -> { return x + 1; }));}));
}
  public static void main(String args[]) {
     Object x;
     x  = new TestIt().readOptional(new MaybeExtension<Integer>());
     System.out.println(x);
  }
  
}