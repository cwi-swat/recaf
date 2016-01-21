package recaf.yield;

import java.util.Iterator;
import java.util.function.Supplier;

public class InternalIterableBase<R> implements Iterable<R> {

	Supplier<R> resumption;
	public InternalIterableBase(Supplier<R> supplier) {
		resumption = supplier;
	}

	/* excerpt from dart paper:
	   class _IterableBase extends IterableBase {
		  var _resumption;
		  _IterableBase(this._resumption);
		  Iterator get iterator { return β-1(_resumption()); }
		}
	*/
	public Iterator<R> iterator() {
		return beta_inv(resumption);
	}

	private Iterator<R> beta_inv(Supplier<R> resumption2) {
		throw new UnsupportedOperationException();
	}
}
