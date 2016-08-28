package generated;

import recaf.paper.demo.Debug;

public class TestDebug {
	private static Debug<Integer> alg = new Debug<Integer>() {
	};

	public int x = 3;

	public int dup(int x) {
		return 2 * x;
	}

	private Integer simple() {
		return (Integer) alg
				.Method(alg.Seq(alg.Debug(alg.Invoke(alg.This(this), "dup", alg.Field(alg.This(this), "x"))),
						alg.Return(alg.Invoke(alg.This(this), "dup", alg.Field(alg.This(this), "x")))));
	}

	public static void main(String args[]) {
		TestDebug s = new TestDebug();
		int n = s.simple();
		System.out.println(n);

	}
}