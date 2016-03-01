package recaf.core;

import static recaf.core.EvalJavaHelper.toValue;

import java.lang.reflect.Field;

// todo: extract interface to make useable with fields in expression eval.
public final class ReflectRef<X> implements IRef<X>{
	private Object obj;
	private Field field;
	
	public ReflectRef(Object obj, String fldName) throws NoSuchFieldException, SecurityException {
		this.obj = obj;
		field = obj.getClass().getDeclaredField(fldName);
	}

	
	@Override
	public X value(){
		field.setAccessible(true);
		try {
			return (X) field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}
	
	@Override
	public void setValue(X val){
		field.setAccessible(true);
		try {
			field.set(obj, val);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
