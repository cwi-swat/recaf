package recaf.demo.pegs;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PEGAlg<S, X extends PEGAlg.F<S>> {
	interface F<S> { }
	
	
	F<S> Lit(Supplier<String> x); 
	
	<T, U> F<U> Regexp(Supplier<String> x, Function<String, F<U>> body);
	
	default <T, U> Parser<U> Let(Supplier<Parser<T>> parser, Function<T, Parser<? extends U>> body) {
		return (s, p) -> {
			Result<T> r = parser.get().parse(s, p);
			// this unwrapping has to with contra variance 
			Result<? extends U> r2 = body.apply(r.getValue()).parse(s, r.getPos()); 
			return new Result<U>(r2.getValue(), r2.getPos());
		};
	}
	
	default <T, U> Parser<U> Seq(Parser<T> p1, Parser<U> p2) {
		return (s, p) -> {
			Result<T> r1 = p1.parse(s, p);
			return p2.parse(s, r1.getPos());
		};
	}
	
	default <T, U> Parser<U> Opt(Supplier<T> otherwise, Function<T, Parser<T>> parser, Function<T, Parser<U>> next) {
		return (s, p) -> {
			T init = otherwise.get();
			try {
				Result<? extends T> t = parser.apply(otherwise.get()).parse(s, p);
				return next.apply(t.getValue()).parse(s, t.getPos());
			}
			catch (Fail f) {
				return next.apply(init).parse(s, p);
			}
		};
	}
	
	default <T, U> Parser<U> Star(Supplier<T> t, Function<T, Parser<T>> parser, Function<T, Parser<U>> next) {
		return (s, p) -> {
			int pos = p;
			T cur = t.get();
			while (true) {
				try {
					Result<T> r = parser.apply(cur).parse(s, pos);
					pos = r.getPos();
					cur = r.getValue();
				}
				catch (Fail f) {
					return next.apply(cur).parse(s, pos);
				}
			}
		};
	}
	
	default <T, U> Parser<U> Plus(Supplier<T> t, Function<T, Parser<T>> parser, Function<T, Parser<U>> next) {
		return (s, p) -> {
			int pos = p;
			T cur = t.get();
			Result<T> r = parser.apply(cur).parse(s, pos);
			pos = r.getPos();
			cur = r.getValue();
			while (true) {
				try {
					r = parser.apply(cur).parse(s, pos);
					pos = r.getPos();
					cur = r.getValue();
				}
				catch (Fail f) {
					return next.apply(cur).parse(s, pos);
				}
			}
		};
	}
	
	
	default <T> Parser<T> Choice(List<Parser<T>> alts) {
		return (s, p) -> {
			for (Parser<T> a: alts) {
				try {
					return a.parse(s, p);
				}
				catch (Fail f) {
					continue;
				}
			}
			throw new Fail();
		};
	}
	
	default <T> Parser<T> Alt(Supplier<String> ignored, Parser<T> body) {
		return body;
	}
	
	default <T> Parser<T> Return(Supplier<T> x) {
		return (s, p) -> new Result<T>(x.get(), p);
	}
	
	default <T> Parser<T> Empty() {
		return (s, p) -> new Result<>(null, p);
	}
	
	default <T> Parser<T> ExpStat(Supplier<T> exp) {
		return (s, p) -> {
			exp.get();
			return new Result<>(null, p);
		};
	}
}
