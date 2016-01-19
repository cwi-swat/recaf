import draft.BuilderAlg;
import higher.Async;

public class TestAsync {
	
	/*
	void delayed(BuilderAlg<Async.t> alg) {
		Async<Integer> answer = async {
			System.out.println("Welcome...");
			return 42;
		}
	}
	translates to --->
	*/
	void desugared_delayed() {
		BuilderAlg<Async.t> builder = new AsyncBuilder();

		Async<Integer> answer = Async.prj(builder.Run(builder.Delay(() -> {
			System.out.println("Welcome...");
			return builder.Return(42);
		})));
	}
}