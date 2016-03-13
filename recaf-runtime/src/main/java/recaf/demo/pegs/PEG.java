package recaf.demo.pegs;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import recaf.core.alg.JavaMethodAlg;

public interface PEG<R> extends JavaMethodAlg<Parser<R>, Parser<R>>{

	@Override
	default Parser<R> Method(Parser<R> body) {
		return body;
	}
	
	default Parser<Void> Lit(Supplier<String> x)  {
		return (s, p) -> {
			String lit = x.get();
			if (s.startsWith(lit, p)) {
				return new Result<>(null, p + lit.length());
			}
			throw new Fail();
		};
	}
	
	default <T> Parser<T> Regexp(Supplier<String> x, Function<String, Parser<? extends T>> body) {
		return (s, p) -> {
			Pattern pat = Pattern.compile(x.get());
			Matcher m = pat.matcher(s.substring(p));
			if (m.find()) {
				if (m.start() == 0) {
					String g = m.group();
					// this unwrapping has to with contra variance 
					Result<? extends T> r = body.apply(g).parse(s, p + g.length());
					return new Result<T>(r.getValue(), r.getPos());
				}
			}
			throw new Fail();
		};
	}
	
	default <T, U> Parser<U> Let(Supplier<Parser<T>> parser, Function<? super T, Parser<? extends U>> body) {
		return (s, p) -> {
			Result<T> r = parser.get().parse(s, p);
			// this unwrapping has to do with contra variance 
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
	
	
	default <T> Parser<T> Choice(List<Parser<? extends T>> alts) {
		return (s, p) -> {
			for (Parser<? extends T> a: alts) {
				try {
					Result<? extends T> r = a.parse(s, p);
					return new Result<T>(r.getValue(), r.getPos());
				}
				catch (Fail f) {
					continue;
				}
			}
			throw new Fail();
		};
	}
	
	default <T> Parser<? extends T> Alt(Supplier<String> ignored, Parser<? extends T> body) {
		return body;
	}
	
	default <T> Parser<T> Return(Supplier<? extends T> x) {
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
