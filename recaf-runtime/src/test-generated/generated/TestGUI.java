package generated;
import recaf.demo.direct.GUI;
import recaf.demo.direct.GUI.Render;
import recaf.demo.direct.GUI.Handle;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.io.StringWriter;


public class TestGUI {
   private GUI  alg;

   public TestGUI(GUI alg) {
     this.alg = alg;
   }


    void h2(String title) {
  recaf.core.Ref<String > $title = new recaf.core.Ref<String >(title);
  alg.Method(alg.Tag(() -> "h2", alg.Echo(() -> $title.value)));
}
  
  static Iterable<Integer> range(int n) {
    return new Iterable<Integer>() {
       public Iterator<Integer> iterator() {
         return IntStream.range(0, n).iterator();
       }
    };
  }
  
  static void println(String x) {
    System.out.println(x);
  }
  
  static  void hello(GUI  alg, int n) {
  recaf.core.Ref<Integer> $n = new recaf.core.Ref<Integer>(n);
  alg.Method(alg.Tag(() -> "div", alg.<Integer>ForEach(() -> range($n.value), (recaf.core.Ref<Integer> i) -> alg.Tag(() -> "p", alg.Seq(alg.Echo(() -> "Hello world " + i.value + "!\n"), alg.Button(() -> "Click " + i.value, alg.If(() -> i.value % 2 == 0, alg.ExpStat(() -> { println("even " + i.value); return null; }), alg.ExpStat(() -> { println("odd " + i.value); return null; }))))))));
}
  
  static void sendToServer(String x) {
    System.out.println(x);
  }
  
  public static void main(String args[]) {
    StringWriter w = new StringWriter();
    hello(new Render(w), 10);
    sendToServer(w.toString());
    // ... receive the clicked id; assume it's "id2"
    hello(new Handle("id2"), 10); // prints out even 2
  }

}