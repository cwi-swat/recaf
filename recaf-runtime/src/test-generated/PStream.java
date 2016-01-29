import recaf.iter.Iter;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.*;
   
public class PStream {
	
	public PStream(Iterable<Integer> source) {
		super();
		this.source = source;
	}

	Iterable<Integer> source;

	public static PStream range(int n) {
		return new PStream(rangeIter(n));
	}

	private static Iterable<Integer> rangeIter(int n) {
		return new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return IntStream.range(0, n).iterator();
			}
		};
	}
	
	public PStream filter(Predicate<Integer> pred) {
		return new PStream(filterIter(this.source, pred));
	}

	Iterable<Integer> filterIter(Iterable<Integer> source, Predicate<Integer> pred) {
  Iter<Integer> $alg = new Iter<Integer>();
  return (Iterable<Integer>)$alg.Method($alg.For($alg.Exp(() -> { return source; }), (Integer t) -> {return $alg.If($alg.Exp(() -> { return pred.test(t); }), $alg.Yield($alg.Exp(() -> { return t; })));}));
}
  	
  	public PStream map(Function<Integer, Integer> f) {
		return new PStream(mapIter(this.source, f));
	}
  	
  	Iterable<Integer> mapIter(Iterable<Integer> source, Function<Integer, Integer> f) {
  Iter<Integer> $alg = new Iter<Integer>();
  return (Iterable<Integer>)$alg.Method($alg.For($alg.Exp(() -> { return source; }), (Integer t) -> {return $alg.Yield($alg.Exp(() -> { return f.apply(t); }));}));
}
	
	public Integer sum() { 
		return sumIter(this.source);
	}
	
	private Integer sumIter(Iterable<Integer> source) {
	  Integer acc = 0;
	  for (Integer t: source) {
	       acc+=t;
	  }
	  return acc;
	}
}