package recaf.core.direct;

public interface ICaseEnv {
	boolean eval(Object value, boolean fallThrough, Env env) throws Throwable;
}
