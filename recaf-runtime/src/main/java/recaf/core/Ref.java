package recaf.core;

// todo: extract interface to make useable with fields in expression eval.
public final class Ref<X> implements IRef<X>{
	public X value ;
	public Ref(X value) {
		this.value = value;
	}

	public Ref() {
		this(null);
	}
	
	@Override
	public X value(){
		return value;
	}

	@Override
	public void setValue(X val) {
		value = val;
	}

}
