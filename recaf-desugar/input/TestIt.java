 
import builders.maybe.MaybeExtension;
 
public class Foo {
  
  
  Optional<Integer> maybe() {
  MaybeExtension<?> $alg = new MaybeExtension<?>();   
  return (Optional<Integer>)$alg.Method($alg.If($alg.Exp(() -> { return 3 > 5; }), $alg.Return($alg.Exp(() -> { return 42; }))));
}
  
  
}