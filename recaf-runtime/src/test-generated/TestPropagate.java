
import recaf.propagate.Propagate;

public class TestPropagate {

	static Void println(Object o) {
		System.out.println(o);
		return null;
	}

	Void update(String s) {
		Propagate<String, Void> $alg = new Propagate<String, Void>();
		return (Void) $alg.Method($alg.Seq($alg.Local($alg.Exp(() -> {
			return s;
		}), $alg.ExpStat($alg.Exp(() -> {
			return method();
		}))), $alg.Ask((String x) -> {
			return $alg.Seq($alg.ExpStat($alg.Exp(() -> {
				return println(x);
			})), $alg.Return($alg.Exp(() -> {
				return null;
			})));
		})));
	}

	Void method() {
		return read();
	}

	Void read() {
		Propagate<String, Void> $alg = new Propagate<String, Void>();
		return (Void) $alg.Method($alg.Ask((String s) -> {
			return $alg.Seq($alg.ExpStat($alg.Exp(() -> {
				return println("s = " + s);
			})), $alg.Return($alg.Exp(() -> {
				return null;
			})));
		}));
	}

	public static void main(String args[]) {
		new TestPropagate().update("Hello world!");
	}

}