package generated;
import java.util.*;
import recaf.core.cps.StmtJavaCPS;

public class TestWhile_withBreak {
    private StmtJavaCPS<Integer> alg = new StmtJavaCPS<Integer>() {};
	
	public  Integer  meth() {
  return (Integer )alg.Method(alg.Decl(() -> 0, (recaf.core.Ref<Integer > sum) -> {return alg.Decl(() -> Arrays.asList(1,2,3,4,5).iterator(), (recaf.core.Ref<Iterator<Integer>> list) -> {return alg.Seq(alg.While(() -> list.value.hasNext(), alg.Seq(alg.ExpStat(() -> { sum.value += list.value.next(); return null; }), alg.If(() -> sum.value > 5, alg.Break()))), alg.Return(() -> sum.value));});}));
}
	
	public static void main(String args[]) {
        System.out.println(new TestWhile_withBreak().meth()); //6
	}

}
