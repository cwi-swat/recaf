package recaf.core.alg;

import recaf.core.IRef;

public interface JavaExprAlg<E> {

	E Lit(int n);
	E Lit(double d);
	E Lit(long l);
	E Lit(float f);
	E Lit(String s);
	E Lit(boolean s);
	E Closure(Object lambda);
	E Cond(E c, E t, E e);
	E ArrayAccess(E array, E index);
	E InvokeSuper(E self, String method, E ...args);
	E InvokeSuper(E self, Class<?> clazz, String method, E ...args);
	E Field(E recv, String name);
	E SuperField(String name, Object self);
	E SuperField(Class<?> clazz, String name, Object self);
	E This(Object self);
	E CastPrim(Class<?> clazz, E e);

	E PostIncr(E arg);
	E PostDecr(E arg);
	E Plus(E arg);
	E Not(E arg);
	E Complement(E arg);
	E PreIncr(E arg);
	E PreDecr(E arg);
	E Minus(E arg);

	E Div(E lhs, E rhs);
	E Remain(E lhs, E rhs);
	E Mul(E lhs, E rhs);
	E Minus(E lhs, E rhs);
	E Plus(E lhs, E rhs);
	E RightShift(E lhs, E rhs);
	E URightShift(E lhs, E rhs);
	E LeftShift(E lhs, E rhs);
	E GtEq(E lhs, E rhs);
	E InstanceOf(E lhs, E rhs);
	E Gt(E lhs, E rhs);
	E LtEq(E lhs, E rhs);
	E Lt(E lhs, E rhs);
	E Eq(E lhs, E rhs);
	E NotEq(E lhs, E rhs);
	E And(E lhs, E rhs);
	E ExcOr(E lhs, E rhs);
	E Or(E lhs, E rhs);
	E LazyAnd(E lhs, E rhs);
	E LazyOr(E lhs, E rhs);
	
    E AssignLeftShift(E lhs, E rhs); 
    E AssignOr(E lhs, E rhs); 
    E AssignAnd(E lhs, E rhs); 
    E AssignRightShift(E lhs, E rhs); 
    E AssignRemain(E lhs, E rhs); 
    E AssignPlus(E lhs, E rhs); 
    E AssignExcOr(E lhs, E rhs); 
    E Assign(E lhs, E rhs); 
    E AssignDiv(E lhs, E rhs); 
    E AssignURightShift(E lhs, E rhs); 
    E AssignMinus(E lhs, E rhs); 
    E AssignAdd(E lhs, E rhs); 

    E New(Class<?> clazz, E...args);
	E Invoke(E obj, String method, E... args) throws Exception;
	
	// the ref and val things are the real bindings themselves
	// because of HOAS encoding of binders.
	E Var(String name, IRef<?> ref);
	
	
}
