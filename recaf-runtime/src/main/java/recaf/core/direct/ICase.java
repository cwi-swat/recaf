package recaf.core.direct;

public interface ICase {
	boolean eval(Object value, boolean fallThrough) throws Throwable;
}
