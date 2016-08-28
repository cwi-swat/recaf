package generated;
import recaf.core.cps.StmtJavaCPS;

public class TestSwitch_break_first {
     private StmtJavaCPS<String> alg = new StmtJavaCPS<String>() {};
 
 	public static Void print(String msg) {
		System.out.println(msg);
		return null;
	}  
	
 
	  String  meth() {
  return (String )alg.Method(alg.Decl(() -> 1, (recaf.core.Ref<Integer> number) -> {return alg.Decl(() -> "none", (recaf.core.Ref<String > selection) -> {return alg.Seq(alg.Switch(() -> number.value, alg.Case(1, alg.Seq(alg.ExpStat(() -> { print("one"); return null; }), alg.Break())), alg.Case(2, alg.ExpStat(() -> { print("two"); return null; })), alg.Default(alg.ExpStat(() -> { print("default"); return null; }))), alg.Return(() -> selection.value));});}));
} 
	
	public static void main(String args[]) {
		new TestSwitch_break_first().meth();
  	}
}