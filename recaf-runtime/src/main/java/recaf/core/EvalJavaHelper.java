package recaf.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.beanutils.MethodUtils;

import recaf.core.direct.IEval;

public class EvalJavaHelper {
	public static Object toValue(Object o){
		// TODO Add case for true objcet references
		if (o instanceof IRef)
			return ((IRef) o).value();
		else
			return o;
	}

	public static Method findMethod(Class<?> clazz, String methodName, Object[] args){
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
	
	public static Field findField(Class<?> clazz, String fieldName){
		while (clazz != null) {
		    Field[] fields = clazz.getDeclaredFields();
		    for (Field f : fields){
				if (f.getName() == fieldName){
					return f;
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
		return o==null || MethodUtils.isAssignmentCompatible(methodParameter, o.getClass());
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
	
	public static IEval delayArgument(Object obj){
		return new IEval() {
			
			@Override
			public Object eval() throws Throwable {
				return obj;
			}
		};
	}

	public static Object[] evaluateArguments(IEval[] args) throws Throwable {
		Object[] evaluatedArgs = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			evaluatedArgs[i] = toValue(args[i].eval());
		}
		return evaluatedArgs;
	}
	
	public static IEval[] delayArguments(Object[] args) throws Throwable {
		return Arrays.asList(args).stream().map(EvalJavaHelper::delayArgument).toArray(size -> new IEval[size]);
	}
	
}
