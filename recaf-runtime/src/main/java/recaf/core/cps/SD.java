package recaf.core.cps;

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

public interface SD<R> {
	void accept(String label, K<R> rho, K0 sigma, K<String> brk, K<String> contin, K<Throwable> err);
}