package generated;
import recaf.demo.cps.Propagate;

 
public class TestPropagate {
  private Propagate<Void> alg = new Propagate<Void>();

    void update(String s) {
  recaf.core.Ref<String > $s = new recaf.core.Ref<String >(s);
  alg.Method(alg.Seq(alg.Local(() -> $s.value, alg.ExpStat(() -> { method(); return null; })), alg.Ask((String x) -> { return alg.ExpStat(() -> { System.out.println(x); return null; }); })));
}
  
  void method() {
     read();
  }
  
    void read() {
  alg.Method(alg.Ask((String s) -> { return alg.ExpStat(() -> { System.out.println("s = " + s); return null; }); }));
}
  
  public static void main(String args[]) {
     new TestPropagate().update("Hello world!");
  }
  
}