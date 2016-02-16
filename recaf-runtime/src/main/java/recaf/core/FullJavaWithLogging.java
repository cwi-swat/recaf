package recaf.core;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FullJavaWithLogging<R> extends FullJavaImpl<R> {
	private static Logger logger = Logger.getLogger(FullJavaImpl.class.getCanonicalName());
	
	@Override
	public Supplier<Object> Var(String name, Ref val){ 
		return () -> { 
			Object r = super.Var(name, val).get();
			logger.log(Level.INFO, "Accessing variable "+name+" => "+r);
			return r;
		};
	}

}