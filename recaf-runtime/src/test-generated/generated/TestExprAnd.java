package generated;

import recaf.core.full.FullJavaDirect;

public class TestExprAnd {

	private int i = 0;

	private static FullJavaDirect<Object> alg = new FullJavaDirect<Object>() {};

	private boolean rhs(){
		i++;
		return true;
	}

	private  boolean lazyAnd() {
  return (Boolean)alg.Method(alg.Return(alg.LazyAnd(alg.Lt(alg.Lit(1), alg.Lit(0)), alg.Invoke(alg.This(this), "rhs"))));
}
	
	private  boolean and() {
  return (Boolean)alg.Method(alg.Return(alg.And(alg.Lt(alg.Lit(1), alg.Lit(0)), alg.Invoke(alg.This(this), "rhs"))));
}
	
	public static void main(String args[]) {
		TestExprAnd o = new TestExprAnd();
		System.out.println(o.lazyAnd());
		System.out.println(o.i);
		System.out.println(o.and());
		System.out.println(o.i);	
	}

}
