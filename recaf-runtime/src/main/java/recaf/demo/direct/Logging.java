package recaf.demo.direct;

import java.util.logging.Level;
import java.util.logging.Logger;

import recaf.core.IRef;
import recaf.core.Ref;
import recaf.core.direct.FullJava;
import recaf.core.direct.IEval;
import recaf.core.direct.NoOp;

public interface Logging<R> extends NoOp<R> {
	static Logger logger = Logger.getLogger(FullJava.class.getCanonicalName());
	
	@Override
	default IEval Var(String name, IRef<?> val){ 
		return () -> { 
			Object r = NoOp.super.Var(name, val).eval();
			logger.log(Level.INFO, "Accessing variable "+name+" => "+r);
			return r;
		};
	}

}