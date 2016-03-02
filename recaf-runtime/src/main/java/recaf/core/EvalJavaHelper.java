package recaf.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.MethodUtils;

public class EvalJavaHelper {
	public static Object toValue(Object o){
		// TODO Add case for true objcet references
		if (o instanceof IRef)
			return ((IRef) o).value();
		else
			return o;
	}

	// TODO implement proper overloading methods resolution
	public static Method findMethod(Object o, String methodName, Object[] args){
		Class<?> clazz = o.getClass();
		while (clazz != null) {
		    Method[] methods = clazz.getDeclaredMethods();
		    for (Method m : methods){
				if (m.getName() == methodName){
					if (m.getParameterCount() == args.length){
						if (areAssignable(m.getParameterTypes(), args))
							return m;
					}
				}
			}
			
		    clazz = clazz.getSuperclass();
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

	public static Constructor<?> findConstructor(Class<?> clazz, Object[] args) {
		while (clazz != null) {
		    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		    for (Constructor<?> c : constructors){
				if (c.getParameterCount() == args.length){
					if (areAssignable(c.getParameterTypes(), args))
							return c;
				}
			}
		    clazz = clazz.getSuperclass();
		}
		return null;
	}
	
}
