package recaf.demo.pegs;

import java.util.HashMap;
import java.util.Map;

public class MemoPEG<R> implements PEG<R> {
	private final Map<Integer, Result<R>> memo = new HashMap<>();
	
	// TODO: make a generic memo aspect.
	
	@Override
	public Parser<R> Method(Parser<R> body) {
		Parser<R> parser = PEG.super.Method(body);
		return (s, p) -> {
			if (!memo.containsKey(p)) {
				memo.put(p, parser.parse(s, p));
			}
			return memo.get(p);
		};
	}
}
