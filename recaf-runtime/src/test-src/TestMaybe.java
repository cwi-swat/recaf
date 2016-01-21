
import recaf.maybe.Maybe;
import java.util.Optional;

public class TestMaybe {

	static Void println(Object o) {
		System.out.println(o);
		return null;
	}

	Optional<Integer> maybe() {
		Maybe<Integer> $alg = new Maybe<Integer>();
		return (Optional<Integer>) $alg.Method($alg.<Integer> Decl($alg.Exp(() -> {
			return 3;
		}), x -> {
			return $alg.Seq($alg.ExpStat($alg.Exp(() -> {
				return println("maybe");
			})), $alg.If($alg.Exp(() -> {
				return 7 > 5;
			}), $alg.Seq($alg.ExpStat($alg.Exp(() -> {
				return println("Yes");
			})), $alg.Return($alg.Exp(() -> {
				return 42;
			})))));
		}));
	}

	Optional<Integer> readOptional() {
		Maybe<Integer> $alg = new Maybe<Integer>();
		return (Optional<Integer>) $alg.Method($alg.Maybe($alg.Exp(() -> {
			return maybe();
		}), (Integer x) -> {
			return $alg.Return($alg.Exp(() -> {
				return x + 1;
			}));
		}));
	}

	public static void main(String args[]) {
		Object x;
		x = new TestMaybe().readOptional();
		System.out.println(x);
	}

}