package recaf.core;

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
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public IRef<X> setValue(X val){
		field.setAccessible(true);
		try {
			field.set(obj, val);
			return this;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
