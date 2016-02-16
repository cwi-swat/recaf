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
	E Var(String name, Ref<?> ref);
	E AssignVar(String name, Ref<?> ref, E value);
	<T> E VarFinal(String name, T val);	
	
	// fields and methods
	@SuppressWarnings("unchecked")
	E Call(E recv, String name, E... args);
	E AssignField(E recv, String name, E value);
	E Field(E recv, String name);
	
	E This();
	
	E PostIncr(E var);
	E PreIncr(E var);

	E PostDecr(E var);
	E PreDecr(E var);
	
	// What to do about static methods and fields?
	
}
