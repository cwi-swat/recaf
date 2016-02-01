package recaf.delim;

import java.util.function.Function;

/*
 * Step 1: https://en.wikibooks.org/wiki/Haskell/Continuation_passing_style
 * ---> just transforming continuations of type (a->r)->r to polymorphic (a->b)->c
 * Step 2: http://infoscience.epfl.ch/record/149136/files/icfp113-rompf.pdf (chapter 3.1)
 * */
public class Shift<A, B, C> {
	K<A, B, C> s;
	
	/* 
	 * (previous and not parametric on the return types)
	 * cont :: ((a -> b) -> c) -> Shift a b c
	 */
	public Shift(K<A, B, C> s) {
		super();
		this.s = s;
	}

	/*
	 * (previous and not parametric on the return types)
	 * bind :: ((a -> b) -> c) -> (a -> ((a1 -> b1) -> c1)) -> ((a1 -> b1) -> c)
	 * bind s f = \k -> s $ \x -> f x $ k
	 */
	public <A1, B1, C1 extends B> Shift<A1, B1, C> bind(Function<A, K<A1, B1, C1>> f) {
		return new Shift<A1, B1, C>((Function<A1, B1> k) -> s.apply((A x) -> (f.apply(x)).apply(k)));
	}
	
	/*
	 * map (essentially bind + unit)
	 * */
	public <A1> Shift<A1, B, C> map (Function<A, A1> f) {
		return new Shift<A1, B, C>((Function<A1, B> k) -> s.apply((A x) -> k.apply(f.apply(x))));
	}
	
	public static <A, B, C> Shift<A, B, C> shift (K<A,B,C> k) {
		return new Shift<A,B,C>(k);
	}
	
	public static <A, C> void reset(Shift<A, A, C> c) {
		c.s.apply(x -> x);
	}
}