package generated;
import recaf.core.cps.StmtJavaCPS;

public class TestFor_ordinary {

    private StmtJavaCPS<Integer> alg = new StmtJavaCPS<Integer>() {};
	
      Integer  meth() {
  return (Integer )alg.Method(alg.Decl(() -> 0, (recaf.core.Ref<Integer> sum) -> {return alg.Seq(alg.ForDecl(() -> 0, (recaf.core.Ref<Integer> i) -> alg.ForBody(() -> i.value < 10, alg.Seq(alg.ExpStat(() -> { i.value++; return null; }), alg.Empty()), alg.ExpStat(() -> { sum.value += i.value; return null; }))), alg.Return(() -> sum.value));}));
}  
	
	public static void main(String args[]) {
        System.out.println(new TestFor_ordinary().meth()); //45   
	}
}
