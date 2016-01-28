import recaf.iter.Iter;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.*;

public class TestIter {

	// todo: same fringe problem.
	static Void println(Object o) {
		System.out.println(o);
		return null;
	}

	Iterable<Integer> range(int n) {
		return new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return IntStream.range(0, n).iterator();
			}
		};
	}

	Iterable<Integer> filter(Iterable<Integer> iter, Predicate<Integer> pred) {
		Iter<Integer> $alg = new Iter<Integer>();
		return (Iterable<Integer>) $alg.Method($alg.For($alg.Exp(() -> {
			return iter;
		}), (Integer t) -> {
			return $alg.If($alg.Exp(() -> {
				return pred.test(t);
			}), $alg.Yield($alg.Exp(() -> {
				return t;
			})));
		}));
	}

	Iterable<Integer> subIter() {
		Iter<Integer> $alg = new Iter<Integer>();
		return (Iterable<Integer>) $alg.Method($alg.For($alg.Exp(() -> {
			return range(10);
		}), (Integer i) -> {
			return $alg.Yield($alg.Exp(() -> {
				return i;
			}));
		}));
	}

	Iterable<Integer> coro() {
		Iter<Integer> $alg = new Iter<Integer>();
		return (Iterable<Integer>) $alg.Method($alg.While($alg.Exp(() -> {
			return true;
		}), $alg.YieldFrom($alg.Exp(() -> {
			return filter(subIter(), x -> ((Integer) x) % 2 == 0);
		}))));
	}

	public static void main(String args[]) {
		for (Integer i : new TestIter().coro()) {
			System.out.println("i = " + i);
		}
	}

}
