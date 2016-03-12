package recaf.paper.methods;

import java.util.Optional;

import recaf.paper.stm.IExec;
import recaf.paper.stm.Return;

public
//BEGIN_TO_OPTIONAL
interface ToOptional<R> {
  default Optional<R> Method(IExec s) {
	try { s.exec(); } 
    catch (Return r) { return Optional.of((R)r.value); } 
    catch (Throwable e) { throw new RuntimeException(e); }
    return Optional.empty();
  }
}
//END_TO_OPTIONAL
