package recaf.core.direct;

@FunctionalInterface
public interface IExec {
	void exec(String label) throws Throwable;
}
