package recaf.paper.hop;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import recaf.paper.expr.IEval;
import recaf.paper.stm.IExec;
import recaf.paper.stm.MuJava;

public class Hop1<R> implements MuJava<R, IExec> {
	private ArrayDeque<Boolean> onClient = new ArrayDeque<>();

	boolean onClient() {
		return onClient.peek();
	}
	
	/*
	 * Example
	 * 
	 * bt = new HTMLButton("Click me", #client {
	 *    alert("Clicked");
	 * });
	 * 
	 */
	
	public <T> IEval Client(IExec s) {
		return () -> {
			try {
				onClient.push(true);
				s.exec();
				return nextClientToken();
			}
			finally {
				onClient.pop();
			}
		};
	}

	public <T> IEval Server(IExec s) {
		return () -> {
			try {
				onClient.push(false);
				s.exec();
				return nextClientToken();
			}
			finally {
				onClient.pop();
			}
		};
	}

	
	public IEval Inject(IEval x) {
		return () -> {
			if (onClient()) {
				clientVals.put(nextClientToken(), x.eval());
				return null;
			}
			else {
				throw new AssertionError("cannot inject if in server");
			}
		};
	}

	private Map<String, Object> clientVals = new HashMap<>();
	private int clientTokens = 0;
	private String nextClientToken() {
		return "client" + clientTokens++;
	}

	// RULE
	// if onclient = true, never evaluate an expression (except in inject)
	// since this would induce possible side effects.
	
	@Override
	public IExec Exp(Supplier<Void> e) {
		return () -> {
			if (!onClient()) {
				e.get();
			}
		};
	}

	@Override
	public <T> IExec Decl(Supplier<T> e, Function<T, IExec> s) {
		return () -> {
			if (!onClient()) {
				s.apply(e.get()).exec();
			}
			else {
				s.apply(null).exec();
			}
		};
	}

	@Override
	public <T> IExec For(Supplier<Iterable<T>> e, Function<T, IExec> s) {
		return () -> {
			if (onClient()) {
				s.apply(null).exec();
			}
			else {
				for (T t: e.get()) {
					s.apply(t).exec();
				}
			}
		};
	}

	@Override
	public IExec If(Supplier<Boolean> c, IExec s1, IExec s2) {
		return () -> {
			if (onClient()) {
				s1.exec();
				s2.exec();
			}
			else {
				if (c.get()) {
					s1.exec();
				}
				else {
					s2.exec();
				}
			}
		};
	}

	@Override
	public IExec Return(Supplier<R> e) {
		return () -> {
			if (!onClient()) {
				throw new recaf.paper.stm.Return(e.get());
			}
		};
	}

	@Override
	public IExec Seq(IExec s1, IExec s2) {
		return () -> {
			s1.exec();
			s2.exec();
		};
	}

	@Override
	public IExec Empty() {
		return () -> {};
	}
}
