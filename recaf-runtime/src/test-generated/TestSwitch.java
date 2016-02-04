import recaf.dummy.*;
import recaf.core.Ref;

public class TestSwitch {

  	String  testSwitch() {
  DummyExtension<String> $alg = new DummyExtension<String>();
  return (String )$alg.Method($alg.<Integer>Decl($alg.Exp(() -> { return 3; }), number -> {return $alg.<Ref >Decl($alg.Exp(() -> { return new Ref(); }), selection -> {return $alg.Seq($alg.Return($alg.Exp(() -> { return (String) selection.value; })), $alg.Switch($alg.Exp(() -> { return x; }), $alg.Case($alg.Exp(() -> { return 2; }), $alg.ExpStat($alg.Exp(() -> { return printn(y); }))), $alg.Case($alg.Exp(() -> { return 3; }), $alg.Empty()), $alg.Case($alg.Exp(() -> { return 1; }), $alg.ExpStat($alg.Exp(() -> { return println(x); }))), $alg.Default($alg.ExpStat($alg.Exp(() -> { return bla(); })))));});}));
} 
	
	public static void main(String args[]) {
		String x;
     	x  = new TestSwitch().testSwitch();
     	System.out.println(x);
  	}
}