
import recaf.yield.YieldExtension;

public class TestYield {

	Iterable<Integer> op(YieldExtension<Integer> alg) {
		YieldExtension<Integer> $alg = alg;
		return $alg.Method($alg.Seq($alg.If($alg.Exp(() -> {
			return 1 > 5;
		})))); 
	}

	public static void main(String[] args)  {
		Iterable<Integer> answers = new TestYield().op(new YieldExtension<Integer>());

		for(Integer answer : answers){
			System.out.println(answer);
		}
	}
}