package recaf.core;

import java.util.function.BiConsumer;

//expression denotations
//(T -> Unit) -> Unit
//TODO: this should simply be thunks to avoid eager
//evaluation, no need for CPS here.
//interface ED<T> extends Supplier<T> {
//Let an Exp constructor catch exceptions and call the exception continuation...
//Exp(Supplier<T> e) {
//return (sigma, exc) -> {
//try {
// sigma.accept(e.get());
//}
//catch (Throwable err) {
//  exc.accept(err);
//}
//}

// (T -> Unit) -> (ex -> Unit) -> Unit 
public interface ED<T> extends BiConsumer<K<T>, K<Throwable>> {
	
}