package recaf.demo.pegs;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import recaf.core.alg.JavaMethodAlg;

public class PEG<R> implements JavaMethodAlg<Parser<R>, Parser<R>>{

	// TODO: Aspects: memoization -> packrat, layout interleaving.
	
	@Override
	public Parser<R> Method(Parser<R> body) {
		return body;
	}
	
	public <T> Supplier<T> Exp(Supplier<T> t) {
		return t;
	}
	
	public Parser<?> Lit(Supplier<String> x)  {
		return (s, p) -> {
			String lit = x.get();
//			System.out.println("parsing lit: " + lit + " on " + s + " at " + p);
			if (s.startsWith(lit, p)) {
//				System.out.println("success: parsing lit: " + lit);
				return new Result<>(null, p + lit.length());
			}
//			System.out.println("fail lit");
			throw new Fail();
		};
	}
	
	public <T> Parser<T> Regexp(Supplier<String> x, Function<String, Parser<T>> body) {
		return (s, p) -> {
			Pattern pat = Pattern.compile(x.get());
			Matcher m = pat.matcher(s.substring(p));
			if (m.find()) {
				if (m.start() == 0) {
					String g = m.group();
//					System.out.println("SUCCESS");
					return body.apply(g).parse(s, p + g.length());
				}
			}
//			System.out.println("FAIL regexp");
			throw new Fail();
		};
	}
	
	public <T, U> Parser<U> Let(Supplier<Parser<T>> parser, Function<T, Parser<U>> body) {
		return (s, p) -> {
			Result<T> r = parser.get().parse(s, p);
			return body.apply(r.getValue()).parse(s, r.getPos());
		};
	}
	
	public <T, U> Parser<U> Seq(Parser<T> p1, Parser<U> p2) {
		return (s, p) -> {
			Result<T> r1 = p1.parse(s, p);
			return p2.parse(s, r1.getPos());
		};
	}
//	
//	public <T> Parser<Optional<T>> Opt(Parser<T> parser) {
//		return (s, p) -> {
//			try {
//				Result<T> r = parser.parse(s, p);
//				return new Result<>(Optional.of(r.getValue()), r.getPos());
//			}
//			catch (Fail f) {
//				return new Result<>(Optional.empty(), p);
//			}
//		};
//	}
//	
//	public <T> Parser<List<T>> Plus(Parser<T> parser) {
//		return (s, p) -> {
//			List<T> lst = new ArrayList<>();
//			int pos = p;
//			Result<T> r = parser.parse(s, pos);
//			pos = r.getPos();
//			lst.add(r.getValue());
//			while (true) {
//				try {
//					r = parser.parse(s, pos);
//					pos = r.getPos();
//					lst.add(r.getValue());
//				}
//				catch (Fail f) {
//					return new Result<>(lst, pos);
//				}
//			}
//		};
//	}
	
	public <T, U> Parser<U> Star(Supplier<T> t, Function<T, Parser<T>> parser, Function<T, Parser<U>> next) {
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
	
	
	public <T> Parser<T> Choice(List<Parser<T>> alts) {
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
	
	public <T> Parser<T> Alt(Supplier<String> ignored, Parser<T> body) {
		return body;
	}
	
	public <T> Parser<T> Return(Supplier<? extends T> x) {
		return (s, p) -> new Result<T>(x.get(), p);
	}
	
	public <T> Parser<T> Empty() {
		return (s, p) -> new Result<>(null, p);
	}
	
	public <T> Parser<T> ExpStat(Supplier<T> exp) {
		return (s, p) -> {
			exp.get();
			return new Result<>(null, p);
		};
	}


}
