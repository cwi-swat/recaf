package recaf.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import recaf.core.Ref;
import recaf.core.direct.FullJava;
import recaf.core.direct.IEval;
import recaf.core.direct.NoOp;

public class Logging<R> extends NoOp<R> {
	private static Logger logger = Logger.getLogger(FullJava.class.getCanonicalName());
	
	@Override
	public IEval Var(String name, Ref<Object> val){ 
		return () -> { 
			Object r = super.Var(name, val).eval();
			logger.log(Level.INFO, "Accessing variable "+name+" => "+r);
			return r;
		};
	}

}