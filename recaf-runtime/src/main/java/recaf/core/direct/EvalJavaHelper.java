package recaf.core.direct;

import java.lang.reflect.Method;

import org.apache.commons.lang.reflect.MethodUtils;

import recaf.core.Ref;

public class EvalJavaHelper {
	public static Object toValue(Object o){
		// TODO Add case for true objct references
		if (o instanceof Ref)
			return ((Ref) o).value;
		else
			return o;
	}

	// TODO implement proper overloading methods resolution
	public static Method findMethod(Object o, String methodName, Object[] args){
		Class<?> clazz = o.getClass();
		for (Method m : clazz.getMethods()){
			if (m.getName() == methodName){
				if (m.getParameterCount() == args.length){
					if (areAssignable(m.getParameterTypes(), args))
						return m;
				}
			}
		}
		return null;
	}

	private static boolean areAssignable(Class<?>[] parameterTypes, Object[] args) {
		for (int i=0; i<parameterTypes.length; i++){
			if (!isAssignable(parameterTypes[i], args[i]))
				return false;
		}
		return true;
		
	}

	private static boolean isAssignable(Class<?> methodParameter, Object o) {
		return MethodUtils.isAssignmentCompatible(methodParameter, o.getClass());
	}
	
}
