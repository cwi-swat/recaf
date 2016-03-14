package recaf.paper.methods;

import recaf.paper.stm.IExec;
import recaf.paper.stm.Return;

public interface TPDirect<R> {
	//BEGIN_TYPE_PRESERVING_METHOD
  default R Method(IExec s) {
    try { s.exec(); } 
	  catch (Return r) { return (R)r.value; }
	  catch (Throwable e) { throw new RuntimeException(e); }
    return null;
  }
//END_TYPE_PRESERVING_METHOD
}
