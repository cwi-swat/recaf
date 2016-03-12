package recaf.paper.methods;

import recaf.paper.stm.IExec;
import recaf.paper.stm.Return;

//BEGIN_TYPE_PRESERVING_METHOD
public interface TPDirect<R> {
  default R Method(IExec s) {
    try { s.exec(); } 
	  catch (Return r) { return (R)r.value; }
	  catch (Throwable e) { throw new RuntimeException(e); }
    return null;
  }
}
//END_TYPE_PRESERVING_METHOD