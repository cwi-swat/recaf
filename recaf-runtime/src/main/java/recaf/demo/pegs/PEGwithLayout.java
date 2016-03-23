package recaf.demo.pegs;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// here it shows that Java 8 interfaces are not true mixins (abstract super classes)
// the calls to super are always bound to concrete super interfaces; as a result
// this PEgwithLayout cannot be used with MemoPEG...
public class PEGwithLayout<R> implements PEG<R> {

	/*
	 * Puts layout before tokens (regexp/lit) and inbetween sequences.
	 */
	
	private Parser<Void> layout;

	public PEGwithLayout(String regexp) {
		Pattern pat = Pattern.compile(regexp);
		this.layout = (s, p) -> {
			Matcher m = pat.matcher(s.substring(p));
			if (m.find() && m.start() == 0) {
				return new Result<Void>(null, p + m.group().length());
			}
			throw new Fail();
		};
	}
	
	<T, U> Function<T, Parser<? extends U>> withLayout(Function<T, Parser<? extends U>> parser) {
		return it -> (s, p) -> {
			Result<Void> r = layout.parse(s, p);
			Result<? extends U> r2 = parser.apply(it).parse(s, r.getPos());
			return new Result<U>(r2.getValue(), r2.getPos());
		};
	}
	
	@Override
	public <T> Parser<T> Regexp(Supplier<String> x, Function<String, Parser<? extends T>> body) {
		return PEG.super.Regexp(x, withLayout(body));
	}

	
	@Override
	public Parser<Void> Lit(Supplier<String> x) {
		return Seq(layout, PEG.super.Lit(x));
	}
	
	@Override
	public 
	//BEGIN_SEQ_LAYOUT
	<T, U> Parser<U> Seq(Parser<T> p1, Parser<U> p2) {
		return PEG.super.Seq(p1, PEG.super.Seq(layout, p2));
	}
	//END_SEQ_LAYOUT

	
}
