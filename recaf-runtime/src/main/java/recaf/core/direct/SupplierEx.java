package recaf.core.direct;

import java.util.function.Supplier;

@FunctionalInterface
public interface SupplierEx<T, E extends Throwable> extends Supplier<T> {
	T getWithEx() throws Throwable;
	
    @Override
    default T get() {
    	try {
    		return getWithEx();
		} catch (final Throwable exception) {
			throw new SuspendedEx(exception);
		}
    }
}
