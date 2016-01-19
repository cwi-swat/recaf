import recaf.async.AsyncExtension;
import java.util.concurrent.Future;

public class TestAsync {

	Future<Integer> op(AsyncExtension<Integer> alg) {
		AsyncExtension<Integer> $alg = alg;
		return (Future<Integer>) $alg.Method($alg.Seq($alg.If($alg.Exp(() -> {
			return 1 > 5;
		}), $alg.Return($alg.Exp(() -> {
			return 42;
		}))), $alg.Return($alg.Exp(() -> {
			return 41;
		}))));
	}
}