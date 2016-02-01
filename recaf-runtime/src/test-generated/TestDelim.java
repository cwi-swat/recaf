import java.util.function.Function;

import recaf.delim.Shift;

public class TestDelim {

	public static void main(String[] args) {
		Shift<Integer, Integer, Integer> ctx = new Shift<Integer, Integer, Integer>(
				(Function<Integer, Integer> k) -> k.apply(k.apply(k.apply((7))))).map(x -> {
					int res = x + 1;
					System.out.println(res);
					return res;
				});

		Shift.reset(ctx); // 10
	}

}
