import recaf.dummy.*;

public class TestDummy {

	String testSwitch() {
		DummyExtension<String> $alg = new DummyExtension<String>();
		return (String) $alg.Method($alg.<Integer> Decl($alg.Exp(() -> {
			return 3;
		}), number -> {
			return $alg.<String> Decl(null, selection -> {
				return $alg.Return($alg.Exp(() -> {
					return selection;
				}));
			});
		}));
	}

	public static void main(String args[]) {
		String x;
		x = new TestDummy().testSwitch();
		System.out.println(x);
	}
}