package generated;
import recaf.core.cps.StmtJavaCPS;

public class TestSwitch_nested_default_beginning {
    private StmtJavaCPS<String> alg = new StmtJavaCPS<String>() {};
 
 	public static Void print(String msg) {
		System.out.println(msg);
		return null;
	}  
	
	  String  meth() {
  return (String )alg.Method(alg.Decl(() -> 1, (recaf.core.Ref<Integer> number) -> {return alg.Decl(() -> 3, (recaf.core.Ref<Integer> number2) -> {return alg.Decl(() -> "none", (recaf.core.Ref<String > selection) -> {return alg.Seq(alg.Switch(() -> number.value, alg.Default(alg.ExpStat(() -> { print("with default"); return null; })), alg.Case(1, alg.Switch(() -> number2.value, alg.Case(1, alg.Seq(alg.ExpStat(() -> { print("one"); return null; }), alg.Break())), alg.Default(alg.ExpStat(() -> { print("with default"); return null; })), alg.Case(3, alg.Seq(alg.ExpStat(() -> { print("three"); return null; }), alg.Break())), alg.Case(4, alg.ExpStat(() -> { print("four"); return null; })))), alg.Case(2, alg.ExpStat(() -> { print("two"); return null; }))), alg.Return(() -> selection.value));});});}));
}
	
	public static void main(String args[]) {

        new TestSwitch_nested_default_beginning().meth();
		
  	}
}