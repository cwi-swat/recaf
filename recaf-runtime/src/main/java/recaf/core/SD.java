package recaf.core;

//statement denotations:
//(R -> Unit) -> (() -> Unit) -> Unit
//- 1. continuation for return
//- 2. continuation for fall through
//- 3. continuation for exceptions (todo)
//- 4. continuation for break (todo)
//- 5. continuation for continue (todo)
public interface SD<R> extends TriConsumer<K<R>, K0, K<Throwable>> {
	
}