package generated;
import recaf.core.cps.StmtJavaCPS;

public class TestSwitch_break_default_beginning {
    private StmtJavaCPS<String> alg = new StmtJavaCPS<String>(){};
 
 	public static Void print(String msg) {
		System.out.println(msg);
		return null;
	}  

	  String  meth() {
  return (String )alg.Method(alg.Decl(() -> 2, (recaf.core.Ref<Integer> number) -> {return alg.Decl(() -> "none", (recaf.core.Ref<String > selection) -> {return alg.Seq(alg.Switch(() -> number.value, alg.Default(alg.ExpStat(() -> { print("with default"); return null; })), alg.Case(1, alg.Seq(alg.ExpStat(() -> { print("one"); return null; }), alg.Break())), alg.Case(2, alg.ExpStat(() -> { print("two"); return null; }))), alg.Return(() -> selection.value));});}));
}
	
	public static void main(String args[]) {
		new TestSwitch_break_default_beginning().meth();
  	}
}