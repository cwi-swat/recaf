package recaf.core.definitional;

import recaf.core.Ref;

public interface JavaExprAlg<E> {

	E Lit(int n);
	E Lit(double d);
	E Lit(long l);
	E Lit(float f);
	E Lit(String s);
	E Lit(boolean s);
	
	E Class(String name, Class<?> klass);
	
	// the ref and val things are the real bindings themselves
	// because of HOAS encoding of binders.
	E Var(String name, Ref<Object> ref);
	E AssignVar(String name, Ref<Object> ref, E value);
	<T> E VarFinal(String name, T val);	
	
	// fields and methods
	@SuppressWarnings("unchecked")
	E Call(E recv, String name, E... args);
	E AssignField(E recv, String name, E value);
	E Field(E recv, String name);
	
	E This();
	
	E PostIncr(String name, Ref<Object> r);
	E PreIncr(String name, Ref<Object> r);

	E PostDecr(String name, Ref<Object> r);
	E PreDecr(String name, Ref<Object> r);
	
	E GreaterThan(E l, E r);
	E LessThan(E l, E r);
	E Plus(E l, E r);
	
	// What to do about static methods and fields?
	
}
