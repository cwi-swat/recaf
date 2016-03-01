package recaf.demo.pegs;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PEGwithLayout<R> implements PEG<R> {

	private Parser<Void> layout;

	public PEGwithLayout(String regexp) {
		Pattern pat = Pattern.compile(regexp);
		this.layout = (s, p) -> {
			Matcher m = pat.matcher(s.substring(p));
			if (m.find() && m.start() == 0) {
				return new Result<Void>(null, m.group().length());
			}
			throw new Fail();
		};
	}
	
	<T, U> Function<T, Parser<U>> withLayout(Function<T, Parser<U>> parser) {
		return it -> (s, p) -> {
			Result<Void> r = layout.parse(s, p);
			return parser.apply(it).parse(s, r.getPos());
		};
	}
	
	@Override
	public <T> Parser<T> Regexp(Supplier<String> x, Function<String, Parser<T>> body) {
		return PEG.super.Regexp(x, withLayout(body));
	}
	
	@Override
	public <T, U> Parser<U> Let(Supplier<Parser<T>> parser, Function<T, Parser<U>> body) {
		return PEG.super.Let(parser, withLayout(body));
	}
	
	@Override
	public <T, U> Parser<U> Seq(Parser<T> p1, Parser<U> p2) {
		return PEG.super.Seq(p1, PEG.super.Seq(layout, p2));
	}
	
	@Override
	public <T, U> Parser<U> Star(Supplier<T> t, Function<T, Parser<T>> parser, Function<T, Parser<U>> next) {
		return PEG.super.Star(t, x -> PEG.super.Seq(layout, parser.apply(x)), next);
	}
}
