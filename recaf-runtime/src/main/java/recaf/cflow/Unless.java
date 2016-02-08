package recaf.cflow;
import recaf.core.functional.ED;
import recaf.core.functional.SD;

public class Unless<R> extends CFlow<R> {
	public SD<R> Unless(ED<Boolean> cond, SD<R> body) {
		return If((k, err) -> cond.accept(b -> k.accept(!b), err), body);
	}
}
