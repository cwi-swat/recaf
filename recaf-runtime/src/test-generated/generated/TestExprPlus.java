package generated;

import recaf.core.full.FullJavaDirect;

public class TestExprPlus {

	private static FullJavaDirect<Object> alg = new FullJavaDirect<Object>() {};

	private static  int plus1() {
  return (Integer)alg.Method(alg.Return(alg.Plus(alg.Lit(1), alg.Lit(1))));
}
	
	private static  double plus2() {
  return (Double)alg.Method(alg.Return(alg.Plus(alg.Lit(1), alg.Lit(1.0))));
}
	
	private static  double plus3() {
  return (Double)alg.Method(alg.Return(alg.Plus(alg.Lit(1.0), alg.Lit(1))));
}
	
	private static  double plus4() {
  return (Double)alg.Method(alg.Return(alg.Plus(alg.Lit(1.0), alg.Lit(1))));
}
	
	private static  String  plus5() {
  return (String )alg.Method(alg.Return(alg.Plus(alg.Lit("1"), alg.Lit(1))));
}
	
	private static  String  plus6() {
  return (String )alg.Method(alg.Return(alg.Plus(alg.Lit(1), alg.Lit("1"))));
}	
	
	private static  String  plus7() {
  return (String )alg.Method(alg.Return(alg.Plus(alg.Lit("1"), alg.Lit(1.0))));
}
	
	private static  String  plus8() {
  return (String )alg.Method(alg.Return(alg.Plus(alg.Lit(1.0), alg.Lit("1"))));
}	
	
	private static  String  plus9() {
  return (String )alg.Method(alg.Return(alg.Plus(alg.Lit(1.0f), alg.Lit("1"))));
}
	
	private static  String  plus10() {
  return (String )alg.Method(alg.Return(alg.Plus(alg.Lit("1"), alg.Lit(1.0f))));
}
	
	private static  float plus11() {
  return (Float)alg.Method(alg.Return(alg.Plus(alg.Lit(1.0f), alg.Lit(1))));
}
	
	private static  float plus12() {
  return (Float)alg.Method(alg.Return(alg.Plus(alg.Lit(1), alg.Lit(1.0f))));
}
	
	private static  double plus13() {
  return (Double)alg.Method(alg.Return(alg.Plus(alg.Lit(1.0f), alg.Lit(1.0))));
}
	
	private static  double plus14() {
  return (Double)alg.Method(alg.Return(alg.Plus(alg.Lit(1.0), alg.Lit(1.0f))));
}
	
	private static  float plus15() {
  return (Float)alg.Method(alg.Return(alg.Plus(alg.Lit(1.0f), alg.Lit(1.0f))));
}
	
	private static  String  plus16() {
  return (String )alg.Method(alg.Return(alg.Plus(alg.Lit("1"), alg.Lit("1"))));
}

	public static void main(String args[]) {
		System.out.println(plus1());
		System.out.println(plus2());
		System.out.println(plus3());
		System.out.println(plus4());
		System.out.println(plus5());
		System.out.println(plus6());		
		System.out.println(plus7());
		System.out.println(plus8());	
		System.out.println(plus9());
		System.out.println(plus10());
		System.out.println(plus11());
		System.out.println(plus12());
		System.out.println(plus13());
		System.out.println(plus14());		
		System.out.println(plus15());
		System.out.println(plus16());		
	}

}
