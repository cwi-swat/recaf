package recaf.core;

import java.lang.reflect.Field;

// todo: extract interface to make useable with fields in expression eval.
public final class ReflectRef<X> implements IRef<X>{
	private Object object;
	private Field field;
	private String fieldName;
	
	public ReflectRef(Object obj, String fldName) throws NoSuchFieldException, SecurityException {
		this.object = obj;
		field = obj.getClass().getDeclaredField(fldName);
		fieldName = fldName;
	}

	
	@Override
	public X value(){
		field.setAccessible(true);
		try {
			return (X) field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public IRef<X> setValue(X val){
		field.setAccessible(true);
		try {
			field.set(object, val);
			return this;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	public String getFieldName() {
		return fieldName;
	}


	public Object getObject() {
		return object;
	}

}
