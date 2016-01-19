import java.util.function.Function;
import java.util.function.Supplier;

import draft.BuilderAlg;
import higher.App;
import higher.Async.t;

public class AsyncBuilder implements BuilderAlg<t> {

	@Override
	public <T, U> App<t, U> Bind(App<t, T> app, Function<T, App<t, U>> f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> App<t, T> Delay(Supplier<App<t, T>> f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> App<t, T> Return(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> App<t, T> ReturnFrom(App<t, T> t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> App<t, T> Run(App<t, T> t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> App<t, T> Combine(App<t, T> f, App<t, T> g) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> App<t, T> Yield(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> App<t, T> YieldFrom(App<t, T> t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> App<t, T> While(Supplier<Boolean> body, App<t, T> t) {
		// TODO Auto-generated method stub
		return null;
	}

}
