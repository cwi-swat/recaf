package recaf.demo.direct;

import java.util.logging.Level;
import java.util.logging.Logger;

import recaf.core.direct.IEval;
import recaf.core.full.FullJavaDirect;

public interface Logging<R> extends FullJavaDirect<R> {
	static Logger logger = Logger.getLogger(FullJavaDirect.class.getCanonicalName());
	
	@Override
	default IEval Var(String name, Object val){ 
		return () -> { 
			Object r = FullJavaDirect.super.Var(name, val).eval();
			logger.log(Level.INFO, "Accessing variable "+name+" => "+r);
			return r;
		};
	}

}