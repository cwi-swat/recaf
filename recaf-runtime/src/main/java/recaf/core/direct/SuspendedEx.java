package recaf.core.direct;

public class SuspendedEx extends RuntimeException {

	protected SuspendedEx(Throwable inner) {
		super(inner);
	}
}
