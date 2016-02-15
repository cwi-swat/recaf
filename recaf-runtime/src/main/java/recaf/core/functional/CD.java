package recaf.core.functional;

import java.util.List;

/**
 * Case Denotation
 *
 * matched: if we have matched a case already.
 * value:   the value that is switched upon
 * rho:     continuation for return
 * sigma:   continuation for success (fall through)
 * brk:     continuation for break
 * contin:  continuation for continue 
 * err:     continuation for exceptions
 *
 * @param <R>
 */

public interface CD<R, V> {
	void accept(boolean matched, V v, List<CD<R, V>> rest, K<R> rho, K0 sigma, K<String> brk, K<String> contin, K<Throwable> err);
}
