package recaf.core.functional;

/**
 * Statement Denotation
 * 
 * rho:    continuation for return
 * sigma:  continuation for success (fall through)
 * brk:    continuation for break
 * contin: continuation for continue 
 * ex:     continuation for exceptions
 *
 * @param <R>
 */

public interface SD<R> extends QuinqueConsumer<K<R>, K0, K<String>, K<String>, K<Throwable>> {
	
}