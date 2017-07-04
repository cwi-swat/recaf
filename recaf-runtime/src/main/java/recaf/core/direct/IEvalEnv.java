package recaf.core.direct;

@FunctionalInterface
public interface IEvalEnv {
	Object eval(Env env) throws Throwable;
}
