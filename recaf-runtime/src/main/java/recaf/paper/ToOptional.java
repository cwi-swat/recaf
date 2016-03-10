package recaf.paper;

import java.util.Optional;

//BEGIN_TO_OPTIONAL
interface ToOptional<R> extends MuJavaBase<Optional<R>> {
  default Optional<R> Method(IExec s) {
	try { s.exec(); } 
    catch (Return r) { return Optional.of((R)r.value); } 
    catch (Throwable e) { throw new RuntimeException(e); }
    return Optional.empty();
  }
}
//END_TO_OPTIONAL
