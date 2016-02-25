package recaf.core.direct;

import recaf.core.Ref;

public class EvalJavaHelper {
	public static Object toValue(Object o){
		// TODO Add case for true objct references
		if (o instanceof Ref)
			return ((Ref) o).value;
		else
			return o;
	}
}
