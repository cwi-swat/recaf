package recaf.core;

// todo: extract interface to make useable with fields in expression eval.
public final class Ref<X> {
	public X value ;
	public Ref(X value){
		this.value = value;
	}
}
