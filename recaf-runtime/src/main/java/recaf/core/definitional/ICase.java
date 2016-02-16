package recaf.core.definitional;

public interface ICase {
	boolean eval(Object value, boolean fallThrough) throws Throwable;
}
