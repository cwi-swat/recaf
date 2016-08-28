package generated;
import java.util.*;
import recaf.core.cps.StmtJavaCPS;

public class TestFor_noBreak {
    private StmtJavaCPS<Integer> alg = new StmtJavaCPS<Integer>() {};

	  Integer  meth() {
  return (Integer )alg.Method(alg.Decl(() -> 0, (recaf.core.Ref<Integer > sum) -> {return alg.Decl(() -> Arrays.asList(1,2,3,4,5), (recaf.core.Ref<List<Integer>> list) -> {return alg.Seq(alg.<Integer >ForEach(() -> list.value, (recaf.core.Ref<Integer > i) -> alg.ExpStat(() -> { sum.value += (Integer)i.value; return null; })), alg.Return(() -> sum.value));});}));
}   
	
	public static void main(String args[]) {
        System.out.println(new TestFor_noBreak().meth()); //15   
	}
}
