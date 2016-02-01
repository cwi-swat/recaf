import recaf.delim.ResetExtension;
import recaf.delim.Shift;
import java.util.function.Function;

public class TestReset {

	static Void println(Object o) {
		System.out.println(o);
		return null;
	}

	class Cell {
		public Integer v;

		public Cell(Integer v) {
			this.v = v;
		}
	}

	Integer ex() {
		ResetExtension<Integer> $alg = new ResetExtension<Integer>();
		return (Integer) $alg.Method($alg.<Integer> Decl($alg.Exp(() -> {
			return v1 + 1;
		}), v2 -> {
			return $alg.Seq($alg.ExpStat($alg.Exp(() -> {
				return println(value);
			})), $alg.Return($alg.Exp(() -> {
				return value;
			})));
		}));
	}

	public static void main(String[] args) {
		/*
		 * Shift<Integer, Integer, Integer> ctx = new Shift<Integer, Integer,
		 * Integer>( k -> k.apply(k.apply(k.apply((7))))).map(value -> { Integer
		 * val = value + 1; println(value); return val; });
		 * 
		 * Shift.reset(ctx); // 10
		 */
	}

}
