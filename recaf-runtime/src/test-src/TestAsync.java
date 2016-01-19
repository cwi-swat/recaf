import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.Arrays.asList;

import recaf.async.AsyncExtension;

public class TestAsync {

	Future<Integer> async1(AsyncExtension<Integer> alg) {
		AsyncExtension<Integer> $alg = alg;
		return (Future<Integer>) $alg.Method($alg.Seq(asList($alg.If($alg.Exp(() -> {
			return 1 > 5;
		}), $alg.Return($alg.Exp(() -> {
			return 42;
		}))), $alg.Return($alg.Exp(() -> {
			return 41;
		})))));
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Integer f2 = new TestAsync().async1(new AsyncExtension<Integer>()).get();
		System.out.println(f2);
	}
}