package recaf.asynciter;

import java.util.function.BiConsumer;

public interface ED<T> extends BiConsumer<K<T>, K<Throwable>> {
	
}