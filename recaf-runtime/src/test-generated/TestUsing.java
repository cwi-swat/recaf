
import recaf.using.Using;

public class TestUsing {

  static Void println(Object o) {
    System.out.println(o);
    return null;
  }
  
  class Resource implements AutoCloseable {
     @Override
     public void close() {
       System.out.println("Closing: " + this);
     }
  }
  
  Void  usingExample() {
  Using<Void> $alg = new Using<Void>();
  return (Void )$alg.Method($alg.Using($alg.Exp(() -> { return new Resource(); }), (Resource r) -> {return $alg.Return($alg.Exp(() -> { return null; }));}));
}
  

  public static void main(String args[]) {
     new TestUsing().usingExample();
  }
  
}