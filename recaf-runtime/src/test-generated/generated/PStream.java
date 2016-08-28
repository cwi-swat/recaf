package generated;

import recaf.demo.cps.Iter;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.*;
import java.util.NoSuchElementException;

   
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
	
	public static PStream of(Integer[] array) {
		return new PStream(ofIter(array));
	}

	private static Iterable<Integer> ofIter(Integer[] array) {
		return new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {

		            final int size = array.length;
		            int cursor = 0;
		
		            @Override
		            public boolean hasNext() {
		                return cursor != size;
		            }
		
		            @Override
		            public Integer next() {
		                if (cursor >= size)
		                    throw new NoSuchElementException();
		
		                return array[cursor++];
		            }
		         };
			};
		};
	}
	
	public PStream filter(Predicate<Integer> pred) {
		return new PStream(filterIter(this.source, pred));
	}

    private Iter<Integer> alg = new Iter<Integer>();

	  Iterable<Integer> filterIter(Iterable<Integer> source, Predicate<Integer> pred) {
  recaf.core.Ref<Iterable<Integer>> $source = new recaf.core.Ref<Iterable<Integer>>(source); recaf.core.Ref<Predicate<Integer>> $pred = new recaf.core.Ref<Predicate<Integer>>(pred);
  return (Iterable<Integer>)alg.Method(alg.<Integer >ForEach(() -> $source.value, (recaf.core.Ref<Integer > t) -> alg.If(() -> $pred.value.test(t.value), alg.Yield(() -> t.value))));
}
  	
  	public PStream map(Function<Integer, Integer> f) {
		return new PStream(mapIter(this.source, f));
	}
  	
	  Iterable<Integer> mapIter(Iterable<Integer> source, Function<Integer, Integer> f) {
  recaf.core.Ref<Iterable<Integer>> $source = new recaf.core.Ref<Iterable<Integer>>(source); recaf.core.Ref<Function<Integer, Integer>> $f = new recaf.core.Ref<Function<Integer, Integer>>(f);
  return (Iterable<Integer>)alg.Method(alg.<Integer >ForEach(() -> $source.value, (recaf.core.Ref<Integer > t) -> alg.Yield(() -> $f.value.apply(t.value))));
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