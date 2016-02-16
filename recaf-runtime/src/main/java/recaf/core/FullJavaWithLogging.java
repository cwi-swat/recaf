package recaf.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import recaf.core.direct.IEval;

public class FullJavaWithLogging<R> extends FullJavaImpl<R> {
	private static Logger logger = Logger.getLogger(FullJavaImpl.class.getCanonicalName());
	
	@Override
	public IEval Var(String name, Ref<Object> val){ 
		return () -> { 
			Object r = super.Var(name, val).eval();
			logger.log(Level.INFO, "Accessing variable "+name+" => "+r);
			return r;
		};
	}

}