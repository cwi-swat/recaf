package recaf.core.direct;

@FunctionalInterface
public interface IExecEnv {
	void exec(String label, Env env) throws Throwable;
}
