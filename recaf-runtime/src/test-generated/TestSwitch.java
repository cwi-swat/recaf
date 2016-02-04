import recaf.dummy.*;
import recaf.core.Ref;

public class TestSwitch {

  	String  testSwitch() {
  DummyExtension<String> $alg = new DummyExtension<String>();
  return (String )$alg.Method($alg.<Integer>Decl($alg.Exp(() -> { return 3; }), number -> {return $alg.<Ref >Decl($alg.Exp(() -> { return new Ref(); }), selection -> {return $alg.Return($alg.Exp(() -> { return (String) selection.value; }));});}));
} 
	
	public static void main(String args[]) {
		String x;
     	x  = new TestSwitch().testSwitch();
     	System.out.println(x);
  	}
}